<#import "po-variables.ftl" as poVariable>
"M","N","QSSGSPC","ADI","","${poVariable.ordrId}","","Sales Orders","","${poVariable.custOrdNum}","","","","","","","","","","","","${poVariable.dt2Str}","","","","","","","","","","","${poVariable.ttlOrdrPrice}","","${poVariable.dlvrTo1}","${poVariable.street}","${poVariable.dlvrTo2}","","${poVariable.cty}","${poVariable.state}","${poVariable.pstlCode}","${poVariable.isoCntry}","${poVariable.cntry}","${poVariable.phne_cntryCode}${poVariable.phne_areaCode}${poVariable.phne}","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""
<#assign i=0>
<#list poVariable.supPartIdArr as supPartId>
"D","N","QSSGSPC","ADI","","${poVariable.ordrId}","${(i+1)?c}","","${supPartId}","${poVariable.dcrptnArr[i]}","","","${poVariable.quntyArr[i]}","","","","","","","","","","${poVariable.untCrcncyArr[i]}","${poVariable.ttlPriceArr[i]}","","","","","","","","",""
<#assign i = i+1>
</#list>
