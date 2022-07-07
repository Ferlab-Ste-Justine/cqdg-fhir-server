package bio.ferlab.cqdg.auth.data.custom;

import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;

public class Metadata extends Resource {

  @Override
  public Resource copy() {
    Metadata res = new Metadata();
    super.copyValues(res);
    return res;
  }

  @Override
  public ResourceType getResourceType() {
    return null;
  }
}
