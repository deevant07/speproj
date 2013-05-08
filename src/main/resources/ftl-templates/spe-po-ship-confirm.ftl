<#import "spe-po-sc-var.ftl" as speSCVariable>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE ShipNoticeRequest SYSTEM "http://xml.cXML.org/schemas/cXML/1.2.023/Fulfill.dtd">
<ShipNoticeRequest> 
   <ShipNoticeHeader shipmentID="${speSCVariable.poNumber}" noticeDate="${speSCVariable.aDateTime?iso_local}" shipmentDate="2000-10-14T08:30:19-08:00" deliveryDate="2000-10-18T09:00:00-08:00"> 
      <ServiceLevel xml:lang="en-US">standard</ServiceLevel>
   </ShipNoticeHeader> 
   <ShipControl>
      <CarrierIdentifier domain="SCAC">CUSTOMCO</CarrierIdentifier> 
      <CarrierIdentifier domain="companyName">Federal Express</CarrierIdentifier> 
      <ShipmentIdentifier>${speSCVariable.trackingNum}</ShipmentIdentifier>
    </ShipControl> 
    <ShipNoticePortion>        
       <OrderReference orderID="${speSCVariable.poNumber}">
          <DocumentReference payloadID="${speSCVariable.poNumber}" />
       </OrderReference>
    </ShipNoticePortion>
</ShipNoticeRequest>