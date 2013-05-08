<#macro printStackTraceAsCause trace lcause>
<#assign ltrace = lcause.cause.stackTrace>
<#list trace as eTrace>
<#assign index=ltrace?seq_index_of(eTrace)>
<#if (index > 0) >
<#break/>
</#if>
</#list>
<BR/>Caused by: ${lcause.cause}
<#assign i = 0>
<#if (index > 1)>
<#list ltrace[0..index-1] as eTrace>
<BR/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at ${eTrace}
</#list>
</#if>
<#assign framesInCommon = (ltrace?size - index)>
<#if framesInCommon != 0>
<BR/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...${framesInCommon} more
</#if>
<#if lcause.cause.cause?? >
	<@printStackTraceAsCause trace=ltrace lcause=lcause.cause />
</#if>	
</#macro>