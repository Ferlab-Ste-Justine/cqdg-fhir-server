package bio.ferlab.cqdg.utils;

import bio.ferlab.cqdg.exceptions.RptIntrospectionException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class Helpers {
    
    public static final String BEARER = "Bearer ";

    @NotNull
    public static String extractAccessTokenFromBearer(String bearer) {
        
        if (StringUtils.isBlank(bearer) || !bearer.startsWith(BEARER)) {
            throw new RptIntrospectionException("Missing bearer token in header");
        }
        
        return bearer.split(" ")[1];
    }
}
