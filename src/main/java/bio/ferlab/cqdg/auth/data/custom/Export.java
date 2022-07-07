package bio.ferlab.cqdg.auth.data.custom;

import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;

public class Export extends Resource {

  @Override
  public Resource copy() {
    Export res = new Export();
    super.copyValues(res);
    return res;
  }

  @Override
  public ResourceType getResourceType() {
    return null;
  }
}
