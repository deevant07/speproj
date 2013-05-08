<#import "spe-po-ack-var.ftl" as speAckVariable>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE cXML SYSTEM "http://xml.cXML.org/schemas/cXML/1.2.023/cXML.dtd">
<cXML payloadID="${speAckVariable.poNumber}" xml:lang="en-US" timestamp="${speAckVariable.aDateTime?iso_local}">
   <Response> 
      <Status code="200" text="OK"/>
   </Response>
</cXML>