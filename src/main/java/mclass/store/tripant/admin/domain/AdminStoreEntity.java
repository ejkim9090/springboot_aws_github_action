package mclass.store.tripant.admin.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AdminStoreEntity {

	//상품관리
		private String buyId;
		private String buyDate;
		private String itemCode;
		private String itemName;
		private String itemPriceChar;
		private Integer itemDur;
		private Integer itemSale;
		private String itemColor;
		private String itemSrc;
		
		private String memNick;
		private String memEmail;
}
