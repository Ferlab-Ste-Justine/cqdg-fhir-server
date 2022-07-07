package bio.ferlab.cqdg.context;

import lombok.Data;

import java.util.Locale;

@Data
public class ServiceContext {
    private String userId;
    private Locale locale;

    public static ServiceContext build(String userId, Locale locale){
        ServiceContext sc = ServiceContext.get();
        sc.setUserId(userId);
        sc.setLocale(locale);

        return sc;
    }

    public static ServiceContext get() {
        ServiceContext sc = ThreadLocalServiceContext.getInstance().get();

        if(sc == null){
            sc = new ServiceContext();
            ThreadLocalServiceContext.getInstance().set(sc);
        }

        return sc;
    }
}
