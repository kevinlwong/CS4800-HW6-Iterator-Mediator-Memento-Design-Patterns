import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class User implements IterableByUser {
    private String username;
    private ChatHistory chatHistory;
    private Map<User, Boolean> blockedUsers; // Map to store blocked users

    public User(String username) {
        this.username = username;
        this.chatHistory = new ChatHistory();
        this.blockedUsers = new HashMap<>();
    }

//    public void sendMessage(User recipient, String messageContent) {
//        if (!recipient.blockedUsers.containsKey(this)) {
//            Message message = new Message(this, new User[] { recipient }, messageContent);
//            ChatServer.getInstance().sendMessage(this, recipient, messageContent);
//            // MessageMemento memento = new MessageMemento(message);
//            // memento.setState(message);
//            // lastSentMessage = message;
//            // lastMessageMemento = message.createMemento();
//            chatHistory.addMessage(message);
//            // recipient.chatHistory.addMessage(message);
//
//        } else {
//            System.out.println(recipient.getUsername() + " cannot receive any messages from " + this.username
//                    + " because " + this.username + " has been blocked by " + recipient.getUsername() + ".");
//        }
//
//    }

    public void sendMessage(User recipient, String messageContent) {
        if (!ChatServer.getInstance().getBlockedUsers().containsKey(this)) {
            Message message = new Message(this, new User[] { recipient }, messageContent);
            ChatServer.getInstance().sendMessage(this, recipient, messageContent);
            chatHistory.addMessage(message);
        } else {
            System.out.println(recipient.getUsername() + " cannot receive any messages from " + this.username
                    + " because " + this.username + " has been blocked by " + recipient.getUsername() + ".");
        }

    }

    ///////////////////////////
    // working undo method without memento pattern, still working on it though
    public void undo() {

        Message lastMessage = this.chatHistory.getLastMessage();
        MessageMemento memento = new MessageMemento(lastMessage);
        chatHistory.removeLastMessage();
        System.out.println("Message undone: " + memento.getContent());
        //if it doesn't work then uncomment this below
        //memento.getState().getSender().getChatHistory().addMessage(memento.getState());

//        for (User recipient : lastMessage.getRecipients()) {
//            lastMessage = recipient.chatHistory.getLastMessage();
//            MessageMemento memento = new MessageMemento(lastMessage);
//            memento.restoreFromMemento(this, recipient, lastMessage);
//        }
        
    }

    public void blockUser(User userToBlock) {
        //this.blockedUsers.put(userToBlock, true);
        ChatServer.getInstance().blockUser(this, userToBlock);

    }

    public void unblockUser(User userToUnblock) {
        //blockedUsers.remove(userToUnblock);
        ChatServer.getInstance().unblockUser(this, userToUnblock);
    }

    public void receiveMessage(Message message) {
        if (!blockedUsers.containsKey(message.getSender())) {
            chatHistory.addMessage(message);
        }
    }

    public ChatHistory getChatHistory() {
        return this.chatHistory;
    }

    public List<Message> getChatHistoryForUser(User otherUser) {
        List<Message> userChatHistory = new ArrayList<>();
        for (Message message : chatHistory.getMessageList()) {
            if (message.getSender().equals(otherUser) || Arrays.asList(message.getRecipients()).contains(otherUser)) {
                userChatHistory.add(message);
            }
        }
        return userChatHistory;
        
    }

    public Iterator<Message> iterator(User userToSearchWith) {
        return this.chatHistory.iterator(userToSearchWith);
    }

    public String getUsername() {
        return this.username;
    }
}
