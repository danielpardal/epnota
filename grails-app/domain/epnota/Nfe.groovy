package epnota

class Nfe {

	String 		accessKey 		//access key NFe
	BigDecimal 	totalInvoice	//total invoice <NFe.infNFe.total.ICMSTot.vNF>

    static constraints = {
    	accessKey(unique:true)
    	totalInvoice()
    }
}
