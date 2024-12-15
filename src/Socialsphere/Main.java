package Socialsphere;

import java.util.*;

// Custom List Implementation (Singly Linked List)
class CustomList<T> {
    private Node<T> head;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public CustomList() {
        this.head = null;
        this.size = 0;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public int size() {
        return size;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        if (index == 0) {
            head = head.next;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
        size--;
    }

    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void display() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
}

// AVL Tree Implementation (for Friends Management)
class AVLTree {
    private class Node {
        String username;
        Node left, right;
        int height;

        Node(String username) {
            this.username = username;
            this.left = this.right = null;
            this.height = 1;
        }
    }

    private Node root;

    public AVLTree() {
        root = null;
    }

    public void insert(String username) {
        root = insert(root, username);
    }

    private Node insert(Node node, String username) {
        if (node == null) {
            return new Node(username);
        }

        if (username.compareTo(node.username) < 0) {
            node.left = insert(node.left, username);
        } else if (username.compareTo(node.username) > 0) {
            node.right = insert(node.right, username);
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        int balance = getBalance(node);

        if (balance > 1 && username.compareTo(node.left.username) < 0) {
            return rotateRight(node);
        }

        if (balance < -1 && username.compareTo(node.right.username) > 0) {
            return rotateLeft(node);
        }

        if (balance > 1 && username.compareTo(node.left.username) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && username.compareTo(node.right.username) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;

        return y;
    }

    public boolean contains(String username) {
        return contains(root, username);
    }

    private boolean contains(Node node, String username) {
        if (node == null) {
            return false;
        }

        if (node.username.equals(username)) {
            return true;
        } else if (username.compareTo(node.username) < 0) {
            return contains(node.left, username);
        } else {
            return contains(node.right, username);
        }
    }

    public List<String> inorder() {
        List<String> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(Node node, List<String> result) {
        if (node != null) {
            inorder(node.left, result);
            result.add(node.username);
            inorder(node.right, result);
        }
    }
}

// Post class, now using CustomList for comments and likes
class Post {
    String content;
    String timestamp;
    int likes;
    CustomList<String> comments;

    public Post(String content) {
        this.content = content;
        this.timestamp = new Date().toString();  // Current timestamp
        this.likes = 0;
        this.comments = new CustomList<>();
    }

    public void addLike() {
        likes++;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void display() {
        System.out.println(content + " [Posted at: " + timestamp + "] Likes: " + likes);
        System.out.println("Comments:");
        if (comments.size() == 0) {
            System.out.println("No comments yet.");
        } else {
            for (int i = 0; i < comments.size(); i++) {
                System.out.println("- " + comments.get(i));
            }
        }
    }
}

// User class, using CustomList for posts and friends with AVL Tree
class User {
    String username;
    String bio;
    CustomList<Post> posts;
    AVLTree friends;

    public User(String username, String bio) {
        this.username = username;
        this.bio = bio;
        this.posts = new CustomList<>();
        this.friends = new AVLTree();  // Use AVL Tree to manage friends
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public void addFriend(String friendUsername) {
        friends.insert(friendUsername);
    }

    public boolean isFriend(String friendUsername) {
        return friends.contains(friendUsername);
    }

    public List<String> getFriends() {
        return friends.inorder();
    }
}

// SocialNetwork class integrating CustomList and AVL Tree
class SocialNetwork {
    Map<String, User> users;

    public SocialNetwork() {
        users = new HashMap<>();
    }

    public void addUser(String username, String bio) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, bio));
            System.out.println(username + " has been added to the network.");
        } else {
            System.out.println("User already exists.");
        }
    }

    public void addFriend(String username, String friendUsername) {
        User user = users.get(username);
        User friend = users.get(friendUsername);
        if (user != null && friend != null && !user.isFriend(friendUsername)) {
            user.addFriend(friendUsername);
            friend.addFriend(username);
            System.out.println(friendUsername + " added as a friend to " + username);
        } else {
            System.out.println("Invalid users or already friends.");
        }
    }

    public void createPost(String username, String content) {
        User user = users.get(username);
        if (user != null) {
            Post newPost = new Post(content);
            user.addPost(newPost);
            System.out.println(username + " posted: " + content);
        } else {
            System.out.println("User does not exist.");
        }
    }

    public void likePost(String username, int postIndex) {
        User user = users.get(username);
        if (user != null && postIndex >= 0 && postIndex < user.posts.size()) {
            Post post = user.posts.get(postIndex);
            post.addLike();
            System.out.println(username + " liked a post.");
        } else {
            System.out.println("Invalid post index.");
        }
    }

    public void commentOnPost(String username, int postIndex, String comment) {
        User user = users.get(username);
        if (user != null && postIndex >= 0 && postIndex < user.posts.size()) {
            Post post = user.posts.get(postIndex);
            post.addComment(comment);
            System.out.println(username + " commented: " + comment);
        } else {
            System.out.println("Invalid post index.");
        }
    }

    public void showSocialFeed(String username) {
        User user = users.get(username);
        if (user != null) {
            List<Post> feed = new ArrayList<>();
            for (String friendUsername : user.getFriends()) {
                User friend = users.get(friendUsername);
                if (friend != null) {
                    // Manually adding posts from friends to the feed
                    for (int i = 0; i < friend.posts.size(); i++) {
                        feed.add(friend.posts.get(i));
                    }
                }
            }
            // Sorting the posts by timestamp (latest first)
            feed.sort((p1, p2) -> p2.timestamp.compareTo(p1.timestamp));
            System.out.println(username + "'s Social Feed:");
            if (feed.isEmpty()) {
                System.out.println("No posts in the feed.");
            } else {
                for (Post post : feed) {
                    post.display();
                    System.out.println("-----------------------------");
                }
            }
        }
    }

    public void suggestFriends(String username) {
        User user = users.get(username);
        if (user != null) {
            Map<String, Integer> mutualFriends = new HashMap<>();
            for (String friendUsername : user.getFriends()) {
                User friend = users.get(friendUsername);
                if (friend != null) {
                    for (String friendOfFriend : friend.getFriends()) {
                        if (!friendOfFriend.equals(username) && !user.isFriend(friendOfFriend)) {
                            mutualFriends.put(friendOfFriend, mutualFriends.getOrDefault(friendOfFriend, 0) + 1);
                        }
                    }
                }
            }

            List<Map.Entry<String, Integer>> suggestions = new ArrayList<>(mutualFriends.entrySet());
            suggestions.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

            System.out.println(username + "'s Friend Suggestions:");
            for (Map.Entry<String, Integer> entry : suggestions) {
                System.out.println(entry.getKey() + " (Mutual friends: " + entry.getValue() + ")");
            }
        }
    }

    // Dijkstra's Shortest Path Implementation for User Connections (Friendships)
    public void findShortestPath(String startUser, String endUser) {
        User start = users.get(startUser);
        User end = users.get(endUser);

        if (start == null || end == null) {
            System.out.println("Invalid users.");
            return;
        }

        // Map each user to an index in the graph
        List<String> allUsers = new ArrayList<>(users.keySet());
        int n = allUsers.size();
        int[] distances = new int[n];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[allUsers.indexOf(startUser)] = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(i -> distances[i]));
        pq.add(allUsers.indexOf(startUser));

        while (!pq.isEmpty()) {
            int u = pq.poll();

            for (String friend : users.get(allUsers.get(u)).getFriends()) {
                int v = allUsers.indexOf(friend);
                int weight = 1; // Each friendship is a unit distance

                if (distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    pq.add(v);
                }
            }
        }

        int shortestDistance = distances[allUsers.indexOf(endUser)];
        if (shortestDistance == Integer.MAX_VALUE) {
            System.out.println("No path found between " + startUser + " and " + endUser);
        } else {
            System.out.println("Shortest path between " + startUser + " and " + endUser + " is " + shortestDistance + " steps.");
        }
    }
}

// Main class for running the social network program
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SocialNetwork socialNetwork = new SocialNetwork();

        while (true) {
            System.out.println("\n--- Social Network ---");
            System.out.println("1. Add User");
            System.out.println("2. Add Friend");
            System.out.println("3. Create Post");
            System.out.println("4. Like Post");
            System.out.println("5. Comment on Post");
            System.out.println("6. Show Social Feed");
            System.out.println("7. Suggest Friends");
            System.out.println("8. Find Shortest Path");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter bio: ");
                    String bio = scanner.nextLine();
                    socialNetwork.addUser(username, bio);
                    break;
                case 2:
                    System.out.print("Enter your username: ");
                    String user1 = scanner.nextLine();
                    System.out.print("Enter friend's username: ");
                    String friend1 = scanner.nextLine();
                    socialNetwork.addFriend(user1, friend1);
                    break;
                case 3:
                    System.out.print("Enter username: ");
                    String user2 = scanner.nextLine();
                    System.out.print("Enter post content: ");
                    String content = scanner.nextLine();
                    socialNetwork.createPost(user2, content);
                    break;
                case 4:
                    System.out.print("Enter username: ");
                    String user3 = scanner.nextLine();
                    System.out.print("Enter post index to like: ");
                    int postIndexLike = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    socialNetwork.likePost(user3, postIndexLike);
                    break;
                case 5:
                    System.out.print("Enter username: ");
                    String user4 = scanner.nextLine();
                    System.out.print("Enter post index to comment on: ");
                    int postIndexComment = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter comment: ");
                    String comment = scanner.nextLine();
                    socialNetwork.commentOnPost(user4, postIndexComment, comment);
                    break;
                case 6:
                    System.out.print("Enter username to view feed: ");
                    String user5 = scanner.nextLine();
                    socialNetwork.showSocialFeed(user5);
                    break;
                case 7:
                    System.out.print("Enter username for friend suggestions: ");
                    String user6 = scanner.nextLine();
                    socialNetwork.suggestFriends(user6);
                    break;
                case 8:
                    System.out.print("Enter start username: ");
                    String startUser = scanner.nextLine();
                    System.out.print("Enter end username: ");
                    String endUser = scanner.nextLine();
                    socialNetwork.findShortestPath(startUser, endUser);
                    break;
                case 9:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}