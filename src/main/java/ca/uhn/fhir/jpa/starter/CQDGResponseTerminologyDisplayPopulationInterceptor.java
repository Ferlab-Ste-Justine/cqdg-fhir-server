package ca.uhn.fhir.jpa.starter;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.BaseResponseTerminologyInterceptor;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.IModelVisitor;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public class CQDGResponseTerminologyDisplayPopulationInterceptor extends BaseResponseTerminologyInterceptor {
	private final BaseRuntimeChildDefinition myCodingSystemChild;
	private final BaseRuntimeChildDefinition myCodingCodeChild;
	private final Class<? extends IBase> myCodingType;
	private final BaseRuntimeElementCompositeDefinition<?> myCodingDefinitition;
	private final BaseRuntimeChildDefinition myCodingDisplayChild;
	private final RuntimePrimitiveDatatypeDefinition myStringDefinition;

	public CQDGResponseTerminologyDisplayPopulationInterceptor(IValidationSupport theValidationSupport) {
		super(theValidationSupport);
		this.myCodingDefinitition = (BaseRuntimeElementCompositeDefinition)Objects.requireNonNull(this.myContext.getElementDefinition("Coding"));
		this.myCodingType = this.myCodingDefinitition.getImplementingClass();
		this.myCodingSystemChild = this.myCodingDefinitition.getChildByName("system");
		this.myCodingCodeChild = this.myCodingDefinitition.getChildByName("code");
		this.myCodingDisplayChild = this.myCodingDefinitition.getChildByName("display");
		this.myStringDefinition = (RuntimePrimitiveDatatypeDefinition)this.myContext.getElementDefinition("string");
	}

	@Hook(
		value = Pointcut.SERVER_OUTGOING_RESPONSE,
		order = 110
	)
	public void handleResource(RequestDetails theRequestDetails, IBaseResource theResource) {
		if(theRequestDetails.getRestOperationType() == RestOperationTypeEnum.GET_PAGE){
			theRequestDetails.setRestOperationType(RestOperationTypeEnum.SEARCH_TYPE);
		}
		List<IBaseResource> resources = this.toListForProcessing(theRequestDetails, theResource);
		FhirTerser terser = this.myContext.newTerser();

		for (IBaseResource nextResource : resources) {
			terser.visit(nextResource, new MappingVisitor());
		}

	}

	private class MappingVisitor implements IModelVisitor {
		private MappingVisitor() {
		}

		public void acceptElement(IBaseResource theResource, IBase theElement, List<String> thePathToElement, BaseRuntimeChildDefinition theChildDefinition, BaseRuntimeElementDefinition<?> theDefinition) {
			if (CQDGResponseTerminologyDisplayPopulationInterceptor.this.myCodingType.isAssignableFrom(theElement.getClass())) {
				String system = CQDGResponseTerminologyDisplayPopulationInterceptor.this.myCodingSystemChild.getAccessor().getFirstValueOrNull(theElement).map((t) -> (IPrimitiveType)t).map(IPrimitiveType::getValueAsString).orElse(null);
				String code = CQDGResponseTerminologyDisplayPopulationInterceptor.this.myCodingCodeChild.getAccessor().getFirstValueOrNull(theElement).map((t) -> (IPrimitiveType)t).map(IPrimitiveType::getValueAsString).orElse(null);
				if (StringUtils.isBlank(system) || StringUtils.isBlank(code)) {
					return;
				}

				String display = CQDGResponseTerminologyDisplayPopulationInterceptor.this.myCodingDisplayChild.getAccessor().getFirstValueOrNull(theElement).map((t) -> (IPrimitiveType)t).map(IPrimitiveType::getValueAsString).orElse(null);
				if (StringUtils.isNotBlank(display)) {
					return;
				}

				ValidationSupportContext validationSupportContext = new ValidationSupportContext(CQDGResponseTerminologyDisplayPopulationInterceptor.this.myValidationSupport);
				if (CQDGResponseTerminologyDisplayPopulationInterceptor.this.myValidationSupport.isCodeSystemSupported(validationSupportContext, system)) {
					IValidationSupport.LookupCodeResult lookupCodeResult = CQDGResponseTerminologyDisplayPopulationInterceptor.this.myValidationSupport.lookupCode(validationSupportContext, system, code);
					if (lookupCodeResult != null && lookupCodeResult.isFound()) {
						String newDisplay = lookupCodeResult.getCodeDisplay();
						IPrimitiveType<?> newString = CQDGResponseTerminologyDisplayPopulationInterceptor.this.myStringDefinition.newInstance(newDisplay);
						CQDGResponseTerminologyDisplayPopulationInterceptor.this.myCodingDisplayChild.getMutator().addValue(theElement, newString);
					}
				}
			}

		}
	}
}
