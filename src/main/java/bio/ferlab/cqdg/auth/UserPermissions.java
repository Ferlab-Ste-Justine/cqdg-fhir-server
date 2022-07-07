package bio.ferlab.cqdg.auth;

import lombok.Data;
import org.hl7.fhir.r4.model.Resource;

import java.util.Arrays;

@Data
public class UserPermissions {
    private final Permission<? extends Resource>[] permissions;

    @SafeVarargs
    public UserPermissions(Permission<? extends Resource>... permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "UserPermissions{" +
                "permissions=" + Arrays.toString(permissions) +
                '}';
    }
}