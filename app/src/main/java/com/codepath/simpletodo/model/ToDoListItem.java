package com.codepath.simpletodo.model;

import android.graphics.Color;

import com.codepath.simpletodo.db.ToDoDatabaseHelper;

/**
 * Created by venugopv on 10/29/15.
 */
public class ToDoListItem {
    private int itemID;
    private String itemText;
    private String itemNotes;
    private int itemPriorityID;
    private String itemPriorityText;
    private int itemPriorityColor;
    private String itemDueDate;
    private int status;

    public ToDoListItem(int itemID, String itemText, String itemNotes, int itemPriorityID,
                        String itemDueDate, int status) {
        this.setItemID(itemID);
        this.setItemText(itemText);
        this.setItemPriorityID(itemPriorityID);
        this.setItemDueDate(itemDueDate);
        this.setStatus(status);
        this.setItemNotes(itemNotes);

        switch (this.getItemPriorityID()) {
            case 1:
                this.setItemPriorityText("MEDIUM");
                this.setItemPriorityColor(Color.parseColor("#EE7600"));
                break;
            case 0:
                this.setItemPriorityText("HIGH");
                this.setItemPriorityColor(Color.RED);
                break;
            default:
                this.setItemPriorityText("LOW");
                this.setItemPriorityColor(Color.parseColor("#006400"));
                break;
        }
    }


    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public int getItemPriorityID() {
        return itemPriorityID;
    }

    public void setItemPriorityID(int itemPriorityID) {
        this.itemPriorityID = itemPriorityID;
    }

    public String getItemPriorityText() {
        return itemPriorityText;
    }

    public void setItemPriorityText(String itemPriorityText) {
        this.itemPriorityText = itemPriorityText;
    }

    public int getItemPriorityColor() {
        return itemPriorityColor;
    }

    public void setItemPriorityColor(int itemPriorityColor) {
        this.itemPriorityColor = itemPriorityColor;
    }

    public String getItemDueDate() {
        return itemDueDate;
    }

    public void setItemDueDate(String itemDueDate) {
        this.itemDueDate = itemDueDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getItemNotes() {
        return itemNotes;
    }

    public void setItemNotes(String itemNotes) {
        this.itemNotes = itemNotes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ToDoDatabaseHelper.KEY_ITEM_ID).append("='").append(getItemID()).append("', ")
                .append(ToDoDatabaseHelper.KEY_ITEM_TEXT).append("='").append(getItemText()).append("', ")
                .append(ToDoDatabaseHelper.KEY_ITEM_NOTES).append("='").append(getItemNotes()).append("', ")
                .append(ToDoDatabaseHelper.KEY_ITEM_DUE_DATE).append("='").append(getItemDueDate()).append("', ")
                .append(ToDoDatabaseHelper.KEY_ITEM_PRIORITY).append("='").append(getItemPriorityText()).append("', ")
                .append(ToDoDatabaseHelper.KEY_ITEM_STATUS).append("='").append(getStatus()).append("'");
        return sb.toString();
    }
}
