package com.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.batch.item.ItemPojo;

public class CustomProcesor implements ItemProcessor<ItemPojo, ItemPojo> {

	private static final Logger log = LoggerFactory.getLogger(CustomProcesor.class);

	@Override
	public ItemPojo process(ItemPojo itemIncoming) throws Exception {
		// TODO Auto-generated method stub

		ItemPojo outGoing = new ItemPojo(itemIncoming.getFirstName().toUpperCase(),
				itemIncoming.getLastName().toLowerCase());
		
		log.info("Incoming:",itemIncoming.toString());
		log.info("Outgoing",itemIncoming.toString());
		return outGoing;
	}

}
