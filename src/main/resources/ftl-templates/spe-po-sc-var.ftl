<#assign poNumberNoHyphen = snCsv[0][2]>
<#assign poNumber = (poNumberNoHyphen)?replace("SP","SP-")>
<#assign carrierCode = snCsv[0][6]>
<#assign aDateTime = .now>
<#assign trackingNum = snCsv[0][5]>