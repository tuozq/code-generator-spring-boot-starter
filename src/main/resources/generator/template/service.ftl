<#-- package -->
package ${servicePackage};

<#-- imports -->
<#list serviceImportTypes as importType>
import ${importType};
</#list>
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ${serviceName} extends ${serviceExtends} {
}