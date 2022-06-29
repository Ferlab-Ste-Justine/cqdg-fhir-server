package bio.ferlab.cqdg.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadLocalServiceContext extends ThreadLocal<ServiceContext> {
    private static final ThreadLocalServiceContext INSTANCE = new ThreadLocalServiceContext();

    public static ThreadLocalServiceContext getInstance(){
        return INSTANCE;
    }

    public static void clear(){
        getInstance().remove();
    }
}
