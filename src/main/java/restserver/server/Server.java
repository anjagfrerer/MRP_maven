package restserver.server;
import com.sun.net.httpserver.HttpServer;
import handler.MediaEntryHandler;
import handler.RatingHandler;
import handler.UserHandler;
import persistence.IUserRepository;
import persistence.MediaEntryRepository;
import persistence.RatingRepository;
import persistence.UserRepository;
import service.MediaEntryService;
import service.RatingService;
import service.UserService;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Sets up and starts the HTTP server.
 * Registers the user, media entry, and rating handlers.
 */
public class Server {
    /**
     * Starts the REST server on port 8080.
     * Initializes repositories, services, and handlers for users, media entries, and ratings.
     *
     * @throws IOException if the server fails to start
     */
    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 10);

        // User Setup
        IUserRepository userRepository = UserRepository.getInstance();
        UserService userservice = UserService.getInstance(userRepository);
        UserHandler userHandler = new UserHandler(userservice);

        // MediaEntry Setup
        MediaEntryRepository mediaEntryRepository = MediaEntryRepository.getInstance();
        MediaEntryService mediaEntryService = MediaEntryService.getInstance(mediaEntryRepository);
        MediaEntryHandler mediaEntryHandler = new MediaEntryHandler(mediaEntryService);

        // Rating Setup
        RatingRepository ratingRepository = RatingRepository.getInstance();
        RatingService ratingService = RatingService.getInstance(ratingRepository);
        RatingHandler ratingHandler = new RatingHandler(ratingService);

        server.createContext("/api/users", userHandler);
        server.createContext("/api/media", mediaEntryHandler);
        server.createContext("/api/ratings", ratingHandler);

        server.start();
    }
}