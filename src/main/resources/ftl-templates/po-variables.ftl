<#assign ordrHdrReq = doc["cXML/Request/OrderRequest/OrderRequestHeader"]>
<#assign ordrId = (ordrHdrReq.@orderID)?replace("-","")>
<#assign ordrDate = ordrHdrReq.@orderDate>
<#assign dlvrTo = doc["cXML/Request/OrderRequest/OrderRequestHeader/ShipTo/Address/PostalAddress/DeliverTo"]>
<#assign street = doc["cXML/Request/OrderRequest/OrderRequestHeader/ShipTo/Address/PostalAddress/Street"]>
<#assign cty = doc["cXML/Request/OrderRequest/OrderRequestHeader/ShipTo/Address/PostalAddress/City"]>
<#assign state = doc["cXML/Request/OrderRequest/OrderRequestHeader/ShipTo/Address/PostalAddress/State"]>
<#assign pstlCode = doc["cXML/Request/OrderRequest/OrderRequestHeader/ShipTo/Address/PostalAddress/PostalCode"]>
<#assign cntry = doc["cXML/Request/OrderRequest/OrderRequestHeader/ShipTo/Address/PostalAddress/Country"]>
<#assign isoCntry = cntry.@isoCountryCode>
<#if doc["cXML/Request/OrderRequest/OrderRequestHeader/Contact/Phone/TelephoneNumber/Number"]?is_node>
    <#assign phne = doc["cXML/Request/OrderRequest/OrderRequestHeader/Contact/Phone/TelephoneNumber/Number"]/>
<#else>
    <#assign phne = "">
</#if>
<#if doc["cXML/Request/OrderRequest/OrderRequestHeader/Contact/Phone/TelephoneNumber/CountryCode"]?is_node>
    <#assign phne_cntryCode = doc["cXML/Request/OrderRequest/OrderRequestHeader/Contact/Phone/TelephoneNumber/CountryCode"]/>
<#else>
    <#assign phne_cntryCode = ""/>
</#if>
<#if doc["cXML/Request/OrderRequest/OrderRequestHeader/Contact/Phone/TelephoneNumber/AreaOrCityCode"]?is_node>
    <#assign phne_areaCode = doc["cXML/Request/OrderRequest/OrderRequestHeader/Contact/Phone/TelephoneNumber/AreaOrCityCode"]/>
<#else>
    <#assign phne_areaCode = ""/>
</#if>
<#if dlvrTo[0]??>
	<#assign dlvrTo1=dlvrTo[0]>
<#else>
	<#assign dlvrTo1="">
</#if>
<#if dlvrTo[1]??>
	<#assign dlvrTo2=dlvrTo[1]>
<#else>
	<#assign dlvrTo2="">
</#if>
<#assign custOrdNum = ordrHdrReq.Extrinsic>
<#assign str2Dt=ordrDate?date("yyyy-MM-dd")>
<#assign dt2Str=str2Dt?string("MM/dd/yy")>

<#assign ttlOrdrPrice=0>
<#assign supPartIdArr= []>
<#assign dcrptnArr= []>
<#assign quntyArr= []>
<#assign untCrcncyArr= []>
<#assign ttlPriceArr=[]>
<#assign reqst = doc["cXML/Request/OrderRequest"]>
<#list reqst?children as itmOut>
<#if itmOut?node_name = 'ItemOut'>
	<#assign qunty=itmOut.@quantity?number>
	<#assign quntyArr=quntyArr+[qunty]>
	<#assign supPartIdArr=supPartIdArr+[itmOut.ItemID.SupplierPartID]>
	<#assign dcrptnArr=dcrptnArr+[itmOut.ItemDetail.Description]>
	<#assign itmDtl=itmOut.ItemDetail>
	<#list itmDtl.Extrinsic as extElem>
		<#if extElem.@name == "retail">
			<#assign rtlPrice=extElem?number>
		<#elseif extElem.@name == "retailCurrencyCode">
			<#assign untCrcncy=extElem>
			<#assign untCrcncyArr=untCrcncyArr+[untCrcncy]>
		</#if>
	</#list>	
	<#assign ttlPrice=rtlPrice*qunty>	
	<#assign ttlOrdrPrice=ttlOrdrPrice+ttlPrice>	
	<#assign ttlPriceArr=ttlPriceArr+[ttlPrice]>		
</#if>
</#list>
<#assign ttlOrdrPrice=ttlOrdrPrice?number?round>