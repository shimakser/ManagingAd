package by.shimakser;

import com.sun.istack.NotNull;
import org.testcontainers.containers.GenericContainer;

public class MongoDbContainer extends GenericContainer<MongoDbContainer> {

    public static final int MONGODB_PORT = 27017;
    public static final String MONGO_IMAGE_VERSION = "mongo:4.2.17";

    public MongoDbContainer() {
        this(MONGO_IMAGE_VERSION);
    }

    public MongoDbContainer(@NotNull String image) {
        super(image);
        addExposedPort(MONGODB_PORT);
    }
}
