package com.wirelessuda.model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * µ×²ã½øÐÐÉ¨Ãè
 * @author ppf
 *
 */
public class RSSHandler extends DefaultHandler {

private final static int TITLE = 1;

private final static int LINK = 2;

private final static int DESCRIPTION = 3;

private final static int PUBDATE = 4;
private final static int DEPART = 5;

private RSSFeed feed;

private RSSItem item;

private int type;


public RSSFeed getFeed(){

return feed;

}

@Override

public void characters(char[] ch, int start, int length) throws SAXException {

	String s = new String(ch, start, length);
	
	switch (type) {
	
	case TITLE:
	
	item.setTitle(s);
	
	type = 0;
	
	break;
	
	case LINK:
	
	item.setLink(s);
	
	type = 0;
	
	break;
	
	case DESCRIPTION:
	
	item.setDescription(s);
	
	type = 0;
	
	break;
	
	case PUBDATE:
	
	item.setPubDate(s);
	
	type = 0;
	
	break;
	case DEPART:
		
		item.setDepart(s);
		
		type = 0;
		
		break;
	
	}

}

@Override
public void endElement(String uri, String localName, String qName)

throws SAXException {

if(localName.equals("item")) {

feed.addRSSItem(item);

}

}

@Override
public void startDocument() throws SAXException {

feed = new RSSFeed();

item = new RSSItem();

}

@Override
public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {

if(localName.equals("item")) {

type = 0;

item = new RSSItem();

}

if(localName.equals("title")) {

type =TITLE;

return;

}

if(localName.equals("link")) {

type =LINK;

return;

}

if(localName.equals("description")) {

type =DESCRIPTION;

return;

}
if(localName.equals("creator")) {

type =DEPART;

return;

}
if(localName.equals("date")) {

type =PUBDATE;

return;

}

type = 0;

}
}
