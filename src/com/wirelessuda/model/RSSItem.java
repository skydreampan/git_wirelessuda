package com.wirelessuda.model;

/**
 * ��xml��Ӧ��ʵ��
 * @author ppf
 *
 */
public class RSSItem {

public final static String TITLE = "title";

public final static String LINK = "link";

public final static String DESCRIPTION = "description";

public final static String PUBDATE = "pubdate";
public final static String DEPART = "depart";

private String title;

private String link;

private String description;

private String pubDate;
private String depart;


public String getDepart() {
	return depart;
}

public void setDepart(String depart) {
	this.depart = depart;
}

public String getTitle(){

return title;

}

public void setTitle(String title) {

this.title =title;

}

public String getLink() {

return link;

}

public void setLink(String link) {

this.link = link;

}

public String getDescription() {

return description;

}

public void setDescription(String description) {

this.description= description;

}

public String getPubDate() {


return pubDate;

}

public void setPubDate(String pubDate) {

this.pubDate =pubDate;

}
}