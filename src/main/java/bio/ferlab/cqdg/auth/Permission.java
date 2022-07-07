package bio.ferlab.cqdg.auth;

import lombok.Data;
import org.hl7.fhir.r4.model.BaseResource;

@Data
public class Permission <T extends BaseResource> {
	public final Class<T> resourceType;
	public final boolean create;
	public final boolean read;
	public final boolean update;
	public final boolean delete;

}
