package persistence;

import database.DatabaseManager;
import model.MediaEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Repository class that manages all media entries.
 */
public class MediaEntryRepository implements IMediaEntryRepository {

    private static final MediaEntryRepository instance = new MediaEntryRepository();

    /** Private constructor to prevent creating multiple instances. */
    private MediaEntryRepository() {
    }

    /**
     * Returns the single instance of the repository.
     *
     * @return the shared MediaEntryRepository instance
     */
    public static MediaEntryRepository getInstance() {
        return instance;
    }

    /**
     * Returns all media entries.
     *
     * @return list of all media entries
     */
    @Override
    public List<MediaEntry> getALlMediaEntries() {
        List<MediaEntry> result = new ArrayList<>();

        String sql = """
            SELECT mediaentryid, title, description, media_type,
                   release_year, age_restriction, AVG(rating.stars) AS avg_score, creator
            FROM mediaentry LEFT JOIN rating ON mediaentry.mediaentryid = rating.mediaentryid GROUP BY mediaentry.mediaentryid
        """;

        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MediaEntry mediaEntry = new MediaEntry();
                mediaEntry.setMediaentryid(rs.getInt("mediaentryid"));
                mediaEntry.setTitle(rs.getString("title"));
                mediaEntry.setDescription(rs.getString("description"));
                mediaEntry.setMediaType(rs.getString("media_type"));
                mediaEntry.setReleaseYear(rs.getInt("release_year"));
                mediaEntry.setAgeRestriction(rs.getInt("age_restriction"));
                mediaEntry.setAvgscore(rs.getInt("avg_score"));
                mediaEntry.setCreatorId(rs.getInt("creator"));

                result.add(mediaEntry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Adds a new media entry and assigns it a random unique ID.
     *
     * @param mediaEntry the media entry to add
     */
    @Override
    public boolean addMediaEntry(MediaEntry mediaEntry) {
        String insertMediaEntry = """
        INSERT INTO mediaentry
        (title, description, media_type, release_year, age_restriction, creator)
        VALUES (?, ?, ?, ?, ?, ?)
        RETURNING mediaentryid
    """;

        String insertGenre = "INSERT INTO genre (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        String selectGenreId = "SELECT genreid FROM genre WHERE name = ?";
        String insertMediaGenre = "INSERT INTO mediaentry_genre (mediaentryid, genreid) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.INSTANCE.getConnection()) {
            conn.setAutoCommit(false);

            int mediaEntryId;
            try (PreparedStatement ps = conn.prepareStatement(insertMediaEntry)) {
                ps.setString(1, mediaEntry.getTitle());
                ps.setString(2, mediaEntry.getDescription());
                ps.setString(3, mediaEntry.getMediaType());
                ps.setInt(4, mediaEntry.getReleaseYear());
                ps.setInt(5, mediaEntry.getAgeRestriction());
                ps.setInt(6, mediaEntry.getCreatorId());

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    mediaEntryId = rs.getInt("mediaentryid");
                }
            }

            for (String genreName : mediaEntry.getGenres()) {
                try (PreparedStatement ps = conn.prepareStatement(insertGenre)) {
                    ps.setString(1, genreName);
                    ps.executeUpdate();
                }

                int genreId;
                try (PreparedStatement ps = conn.prepareStatement(selectGenreId)) {
                    ps.setString(1, genreName);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) continue;
                        genreId = rs.getInt("genreid");
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(insertMediaGenre)) {
                    ps.setInt(1, mediaEntryId);
                    ps.setInt(2, genreId);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a media entry by title and type.
     *
     * @param id id of the media
     * @return
     */
    @Override
    public boolean deleteMediaEntry(int id) {
        String sql = "DELETE FROM mediaentry WHERE mediaentryid = ?";

        try (Connection connection = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Finds a media entry by its ID.
     *
     * @param id the ID of the media entry
     * @return the found media entry
     */
    @Override
    public MediaEntry getMediaEntryByID(int id) {
        String sql = "SELECT m.mediaentryid, m.title, m.description, m.media_type, m.release_year, m.age_restriction, m.creator, AVG(r.stars) AS avg_score, STRING_AGG(DISTINCT g.name, ',') AS genres FROM mediaentry m LEFT JOIN rating r ON m.mediaentryid = r.mediaentryid LEFT JOIN mediaentry_genre mg ON m.mediaentryid = mg.mediaentryid LEFT JOIN genre g ON mg.genreid = g.genreid WHERE m.mediaentryid = ? GROUP BY m.mediaentryid, m.title, m.description, m.media_type, m.release_year, m.age_restriction, m.creator";

        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MediaEntry mediaEntry = new MediaEntry();
                    mediaEntry.setMediaentryid(rs.getInt("mediaentryid"));
                    mediaEntry.setTitle(rs.getString("title"));
                    mediaEntry.setDescription(rs.getString("description"));
                    mediaEntry.setMediaType(rs.getString("media_type"));
                    mediaEntry.setReleaseYear(rs.getInt("release_year"));
                    mediaEntry.setAgeRestriction(rs.getInt("age_restriction"));
                    mediaEntry.setAvgscore(rs.getDouble("avg_score"));
                    mediaEntry.setCreatorId(rs.getInt("creator"));

                    // Genres als List setzen
                    String genresStr = rs.getString("genres");
                    List<String> genres = genresStr != null ? List.of(genresStr.split(",")) : new ArrayList<>();
                    mediaEntry.setGenres(genres);

                    return mediaEntry;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates an existing media entry with new data.
     *
     * @param id the ID of the media entry
     * @param title new title
     * @param description new description
     * @param mediatype new type
     * @param genres new genres
     * @param releaseYear new release year
     * @param agerestriction new age restriction
     * @param creatorId user who wrote the entry
     */
    @Override
    public boolean updateMediaEntry(int id, String title, String description, String mediatype,
                                    List<String> genres, int releaseYear, int agerestriction, int creatorId) {

        String sql = "UPDATE mediaentry SET title = ?, description = ?, media_type = ?, release_year = ?, age_restriction = ?, creator = ? WHERE mediaentryid = ?";

        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, mediatype);
            ps.setInt(4, releaseYear);
            ps.setInt(5, agerestriction);
            ps.setInt(6, creatorId);
            ps.setInt(7, id);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Changes the favorite status of a media entry.
     *
     * @param id the ID of the media entry
     * @param favorite true to mark as favorite, false otherwise
     */
    @Override
    public boolean setFavoriteStatus(int userid, int mediaentryid) {
        String sql = "INSERT INTO favorite (userid, mediaentryid) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userid);
            ps.setInt(2, mediaentryid);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean setUnFavoriteStatus(int userid, int mediaentryid) {
        String sql = "DELETE FROM favorite WHERE userid = ? AND mediaentryid = ?";

        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userid);
            ps.setInt(2, mediaentryid);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MediaEntry> searchAndFilterMediaEntries(String title, String genre, String sortBy) {
        StringBuilder sql = new StringBuilder(
                "SELECT m.mediaentryid, m.title, m.description, m.media_type, m.release_year, m.age_restriction, m.creator, " +
                        "AVG(r.stars) AS avg_score, STRING_AGG(DISTINCT g.name, ',') AS genres " +
                        "FROM mediaentry m " +
                        "LEFT JOIN rating r ON m.mediaentryid = r.mediaentryid " +
                        "LEFT JOIN mediaentry_genre mg ON m.mediaentryid = mg.mediaentryid " +
                        "LEFT JOIN genre g ON mg.genreid = g.genreid " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            sql.append(" AND LOWER(m.title) LIKE ?");
            params.add("%" + title.toLowerCase() + "%");
        }

        if (genre != null && !genre.isBlank()) {
            sql.append(" AND EXISTS (SELECT 1 FROM mediaentry_genre mg2 JOIN genre g2 ON mg2.genreid = g2.genreid " +
                    "WHERE mg2.mediaentryid = m.mediaentryid AND g2.name = ?)");
            params.add(genre);
        }

        sql.append(" GROUP BY m.mediaentryid, m.title, m.description, m.media_type, m.release_year, m.age_restriction, m.creator");

        if (sortBy != null) {
            switch (sortBy) {
                case "title" -> sql.append(" ORDER BY m.title");
                case "year" -> sql.append(" ORDER BY m.release_year");
                case "score" -> sql.append(" ORDER BY avg_score DESC");
                default -> throw new IllegalArgumentException("Invalid sortBy");
            }
        }

        List<MediaEntry> result = new ArrayList<>();

        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MediaEntry m = new MediaEntry();
                    m.setMediaentryid(rs.getInt("mediaentryid"));
                    m.setTitle(rs.getString("title"));
                    m.setDescription(rs.getString("description"));
                    m.setMediaType(rs.getString("media_type"));
                    m.setReleaseYear(rs.getInt("release_year"));
                    m.setAgeRestriction(rs.getInt("age_restriction"));
                    m.setAvgscore(rs.getDouble("avg_score"));
                    m.setCreatorId(rs.getInt("creator"));

                    // Genres aufteilen
                    String genresStr = rs.getString("genres");
                    List<String> genres = genresStr != null ? List.of(genresStr.split(",")) : new ArrayList<>();
                    m.setGenres(genres);

                    result.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<MediaEntry> fullSearchAndFilterMediaEntries(Map<String, Object> filters, String sortBy) {
        StringBuilder sql = new StringBuilder(
                "SELECT m.mediaentryid, m.title, m.description, m.media_type, m.release_year, m.age_restriction, m.creator, " +
                        "AVG(r.stars) AS avg_score, STRING_AGG(DISTINCT g.name, ',') AS genres " +
                        "FROM mediaentry m " +
                        "LEFT JOIN rating r ON m.mediaentryid = r.mediaentryid " +
                        "LEFT JOIN mediaentry_genre mg ON m.mediaentryid = mg.mediaentryid " +
                        "LEFT JOIN genre g ON mg.genreid = g.genreid " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (filters.containsKey("title")) {
            sql.append(" AND LOWER(m.title) LIKE ?");
            params.add("%" + filters.get("title").toString().toLowerCase() + "%");
        }
        if (filters.containsKey("genre")) {
            sql.append(" AND EXISTS (SELECT 1 FROM mediaentry_genre mg2 JOIN genre g2 ON mg2.genreid = g2.genreid " +
                    "WHERE mg2.mediaentryid = m.mediaentryid AND g2.name = ?)");
            params.add(filters.get("genre"));
        }
        if (filters.containsKey("mediaType")) {
            sql.append(" AND LOWER(m.media_type) LIKE ?");
            params.add("%" + filters.get("mediaType").toString().toLowerCase() + "%");
        }
        if (filters.containsKey("releaseYear")) {
            sql.append(" AND m.release_year = ?");
            params.add(filters.get("releaseYear"));
        }
        if (filters.containsKey("ageRestriction")) {
            sql.append(" AND m.age_restriction <= ?");
            params.add(filters.get("ageRestriction"));
        }

        sql.append(" GROUP BY m.mediaentryid, m.title, m.description, m.media_type, m.release_year, m.age_restriction, m.creator");

        if (sortBy != null) {
            switch (sortBy) {
                case "title" -> sql.append(" ORDER BY m.title");
                case "year" -> sql.append(" ORDER BY m.release_year");
                case "score" -> sql.append(" ORDER BY avg_score DESC");
                default -> throw new IllegalArgumentException("Invalid sortBy");
            }
        }

        List<MediaEntry> result = new ArrayList<>();
        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MediaEntry m = new MediaEntry();
                    m.setMediaentryid(rs.getInt("mediaentryid"));
                    m.setTitle(rs.getString("title"));
                    m.setDescription(rs.getString("description"));
                    m.setMediaType(rs.getString("media_type"));
                    m.setReleaseYear(rs.getInt("release_year"));
                    m.setAgeRestriction(rs.getInt("age_restriction"));
                    m.setAvgscore(rs.getDouble("avg_score"));
                    m.setCreatorId(rs.getInt("creator"));

                    // Genres als Liste
                    String genresStr = rs.getString("genres");
                    List<String> genres = genresStr != null ? List.of(genresStr.split(",")) : new ArrayList<>();
                    m.setGenres(genres);

                    result.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<MediaEntry> getRecommendationByGenre(int userid) {
        String sql = "SELECT m.mediaentryid, m.title, m.description, m.media_type, m.release_year, m.age_restriction, m.creator, AVG(r2.stars) AS avg_score, STRING_AGG(DISTINCT g.name, ',') AS genres FROM mediaentry m JOIN mediaentry_genre mg ON m.mediaentryid = mg.mediaentryid LEFT JOIN rating r2 ON m.mediaentryid = r2.mediaentryid LEFT JOIN mediaentry_genre mg3 ON m.mediaentryid = mg3.mediaentryid LEFT JOIN genre g ON mg3.genreid = g.genreid WHERE mg.genreid IN ( SELECT DISTINCT mg2.genreid FROM rating r JOIN mediaentry_genre mg2 ON r.mediaentryid = mg2.mediaentryid WHERE r.creator = ? AND r.stars >= 4 ) AND m.mediaentryid NOT IN ( SELECT mediaentryid FROM rating WHERE creator = ? ) GROUP BY m.mediaentryid, m.title, m.description, m.media_type, m.release_year, m.age_restriction, m.creator ORDER BY avg_score DESC NULLS LAST";
        List<MediaEntry> result = new ArrayList<>();
        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setInt(1, userid);
            ps.setInt(2, userid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MediaEntry m = new MediaEntry();
                    m.setMediaentryid(rs.getInt("mediaentryid"));
                    m.setTitle(rs.getString("title"));
                    m.setDescription(rs.getString("description"));
                    m.setMediaType(rs.getString("media_type"));
                    m.setReleaseYear(rs.getInt("release_year"));
                    m.setAgeRestriction(rs.getInt("age_restriction"));
                    m.setAvgscore(rs.getDouble("avg_score"));
                    m.setCreatorId(rs.getInt("creator"));

                    // Genres als Liste
                    String genresStr = rs.getString("genres");
                    List<String> genres = genresStr != null ? List.of(genresStr.split(",")) : new ArrayList<>();
                    m.setGenres(genres);

                    result.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<MediaEntry> getRecommendationByContent(int userid) {

        String sql = """
        SELECT 
            m.mediaentryid,
            m.title,
            m.description,
            m.media_type,
            m.release_year,
            m.age_restriction,
            m.creator,
            AVG(r2.stars) AS avg_score,
            COUNT(DISTINCT mg.genreid) AS genre_match_count,
            STRING_AGG(DISTINCT g.name, ',') AS genres
        FROM mediaentry m
        JOIN mediaentry_genre mg ON m.mediaentryid = mg.mediaentryid
        JOIN genre g ON mg.genreid = g.genreid

        JOIN mediaentry m_ref ON 
            m.media_type = m_ref.media_type
            AND m.age_restriction = m_ref.age_restriction

        JOIN mediaentry_genre mg_ref 
            ON mg.genreid = mg_ref.genreid
            AND m_ref.mediaentryid = mg_ref.mediaentryid

        JOIN rating r_ref 
            ON r_ref.mediaentryid = m_ref.mediaentryid
            AND r_ref.creator = ?
            AND r_ref.stars >= 4

        LEFT JOIN rating r2 ON m.mediaentryid = r2.mediaentryid

        WHERE m.mediaentryid NOT IN (
            SELECT mediaentryid FROM rating WHERE creator = ?
        )

        GROUP BY 
            m.mediaentryid,
            m.title,
            m.description,
            m.media_type,
            m.release_year,
            m.age_restriction,
            m.creator

        ORDER BY 
            genre_match_count DESC,
            avg_score DESC NULLS LAST
        """;

        List<MediaEntry> result = new ArrayList<>();

        try (Connection conn = DatabaseManager.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userid);
            ps.setInt(2, userid);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MediaEntry m = new MediaEntry();
                    m.setMediaentryid(rs.getInt("mediaentryid"));
                    m.setTitle(rs.getString("title"));
                    m.setDescription(rs.getString("description"));
                    m.setMediaType(rs.getString("media_type"));
                    m.setReleaseYear(rs.getInt("release_year"));
                    m.setAgeRestriction(rs.getInt("age_restriction"));
                    m.setAvgscore(rs.getDouble("avg_score"));
                    m.setCreatorId(rs.getInt("creator"));

                    String genresStr = rs.getString("genres");
                    List<String> genres = genresStr != null
                            ? List.of(genresStr.split(","))
                            : new ArrayList<>();

                    m.setGenres(genres);
                    result.add(m);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}