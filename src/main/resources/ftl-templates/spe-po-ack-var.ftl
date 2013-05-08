<#assign poNumberNoHyphen = snCsv[0][2]>
<#assign poNumber = (poNumberNoHyphen)?replace("SP","SP-")>
<#assign aDateTime = .now>