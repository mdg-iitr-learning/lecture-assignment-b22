package com.amishgarg.wartube.Model;

public class Comment {

    private Author author;
    private String commentText;
    private Object timestamp;

    public Comment()
    {}

    public Comment(Author author, String commentText, Object timestamp) {
        this.author = author;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

}
