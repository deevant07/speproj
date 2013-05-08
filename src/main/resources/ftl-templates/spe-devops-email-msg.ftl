<#import "spe-devops-email-msg-var.ftl" as printCause>
<#assign exMsgObject = exMsgObj>
<#assign frstTrace=exMsgObject.exception.stackTrace>
<B>Exception Raised</B>
<BR/><B>Logged Date Time:</B> ${exMsgObject.timeStamp?datetime}
<BR/><B>From Component:</B> ${exMsgObject.componentName}
<BR/><B>Exception Type:</B> ${exMsgObject.exception.class}
<BR/><B>Exception Message:</B> ${exMsgObject.exception.message}
<BR/><B>Exception Cause:</B> ${exMsgObject.exception.cause}
<BR/><B>Stack Trace:</B>
<#list exMsgObject.exception.stackTrace as trace>
<BR/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;at ${trace}
</#list>
<@printCause.printStackTraceAsCause trace=frstTrace lcause=exMsgObject.exception />
