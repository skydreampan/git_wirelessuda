package com.wirelessuda.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 扫描出的结果放在这里，map
 * @author ppf
 *
 */
public class RSSFeed {

private String title;
private String link;
private String description;
private String pubDate;
private String depart;

public String getPubDate() {
	return pubDate;
}

public void setPubDate(String pubDate) {
	this.pubDate = pubDate;
}

public String getDepart() {
	return depart;
}

public void setDepart(String depart) {
	this.depart = depart;
}

private List<RSSItem> items;

public List getItems() {

List<Map<String,String>> list = new ArrayList<Map<String, String>>();

for (RSSItem item: items) {

Map<String,String> map = new HashMap<String, String>();

map.put(RSSItem.TITLE,item.getTitle()); //取值时，用这个名字取

map.put(RSSItem.LINK,item.getLink());

map.put(RSSItem.DESCRIPTION,item.getDescription());
map.put(RSSItem.DEPART,item.getDepart());
map.put(RSSItem.PUBDATE,item.getPubDate());

list.add(map);

}

return list;

}

public RSSFeed() {

items = new ArrayList<RSSItem>();

}

public void addRSSItem(RSSItem item) {

items.add(item);

}

public String getTitle(){

return title;

}

public void setTitle(String title) {

this.title = title;

}

public String getLink(){

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
}

