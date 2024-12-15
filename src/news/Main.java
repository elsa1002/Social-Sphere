package news;

import java.util.*;

class User {
    String username;
    String bio;
    List<Post> posts;
    Set<User> friends;

    public User(String username, String bio) {
        this.username = username;
        this.bio = bio;
        this.posts = new ArrayList<>();
        this.friends = new HashSet<>();
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public void removeFriend(User friend) {
        friends.remove(friend);
    }
}

class Post {
    String content;
    String timestamp;
    int likes;
    List<String> comments;

    public Post(String content) {
        this.content = content;
        this.timestamp = new Date().toString();  // Current timestamp
        this.likes = 0;
        this.comments = new ArrayList<>();
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
        if (comments.isEmpty()) {
            System.out.println("No comments yet.");
        } else {
            for (String comment : comments) {
                System.out.println("- " + comment);
            }
        }
    }
}

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
        if (user != null && friend != null && !user.friends.contains(friend)) {
            user.addFriend(friend);
            friend.addFriend(user);
            System.out.println(friendUsername + " added as a friend to " + username);
        } else {
            System.out.println("Invalid users or already friends.");
        }
    }

    public void removeFriend(String username, String friendUsername) {
        User user = users.get(username);
        User friend = users.get(friendUsername);
        if (user != null && friend != null && user.friends.contains(friend)) {
            user.removeFriend(friend);
            friend.removeFriend(user);
            System.out.println(friendUsername + " removed as a friend from " + username);
        } else {
            System.out.println("Invalid users or not friends.");
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
            // Get posts from the user's friends
            for (User friend : user.friends) {
                feed.addAll(friend.posts);
            }
            // Sort posts by timestamp (most recent first)
            feed.sort((p1, p2) -> p2.timestamp.compareTo(p1.timestamp));
            System.out.println(username + "'s Social Feed:");
            for (Post post : feed) {
                post.display(); // This will print the post details including likes and comments
                System.out.println("-----------------------------");
            }
        }
    }

    public void findShortestPath(String startUser, String endUser) {
        User start = users.get(startUser);
        User end = users.get(endUser);
        if (start == null || end == null) {
            System.out.println("Invalid users.");
            return;
        }

        Map<User, Integer> distances = new HashMap<>();
        Map<User, User> previous = new HashMap<>();
        PriorityQueue<User> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialize distances
        for (User user : users.values()) {
            distances.put(user, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            User current = pq.poll();
            if (current == end) break;

            for (User friend : current.friends) {
                int newDist = distances.get(current) + 1;
                if (newDist < distances.get(friend)) {
                    distances.put(friend, newDist);
                    previous.put(friend, current);
                    pq.add(friend);
                }
            }
        }

        // Reconstruct the shortest path
        List<User> path = new ArrayList<>();
        User current = end;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        Collections.reverse(path);

        if (distances.get(end) == Integer.MAX_VALUE) {
            System.out.println("No path found.");
        } else {
            System.out.println("Shortest path from " + startUser + " to " + endUser + ":");
            for (User u : path) {
                System.out.println(u.username);
            }
        }
    }

    public void suggestFriends(String username) {
        User user = users.get(username);
        if (user != null) {
            Map<User, Integer> mutualFriends = new HashMap<>();
            for (User friend : user.friends) {
                for (User friendOfFriend : friend.friends) {
                    if (!friendOfFriend.equals(user) && !user.friends.contains(friendOfFriend)) {
                        mutualFriends.put(friendOfFriend, mutualFriends.getOrDefault(friendOfFriend, 0) + 1);
                    }
                }
            }

            List<Map.Entry<User, Integer>> suggestions = new ArrayList<>(mutualFriends.entrySet());
            suggestions.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

            System.out.println(username + "'s Friend Suggestions:");
            for (Map.Entry<User, Integer> entry : suggestions) {
                System.out.println(entry.getKey().username + " (Mutual friends: " + entry.getValue() + ")");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SocialNetwork socialNetwork = new SocialNetwork();

        while (true) {
            System.out.println("\n--- Social Network ---");
            System.out.println("1. Add User");
            System.out.println("2. Add Friend");
            System.out.println("3. Remove Friend");
            System.out.println("4. Create Post");
            System.out.println("5. Like Post");
            System.out.println("6. Comment on Post");
            System.out.println("7. Show Social Feed");
            System.out.println("8. Find Shortest Path");
            System.out.println("9. Suggest Friends");
            System.out.println("10. Exit");
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
                    System.out.print("Enter your username: ");
                    String user2 = scanner.nextLine();
                    System.out.print("Enter friend's username: ");
                    String friend2 = scanner.nextLine();
                    socialNetwork.removeFriend(user2, friend2);
                    break;
                case 4:
                    System.out.print("Enter username: ");
                    String user3 = scanner.nextLine();
                    System.out.print("Enter post content: ");
                    String content = scanner.nextLine();
                    socialNetwork.createPost(user3, content);
                    break;
                case 5:
                    System.out.print("Enter username: ");
                    String user4 = scanner.nextLine();
                    System.out.print("Enter post index to like: ");
                    int postIndexLike = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    socialNetwork.likePost(user4, postIndexLike);
                    break;
                case 6:
                    System.out.print("Enter username: ");
                    String user5 = scanner.nextLine();
                    System.out.print("Enter post index to comment on: ");
                    int postIndexComment = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    System.out.print("Enter comment: ");
                    String comment = scanner.nextLine();
                    socialNetwork.commentOnPost(user5, postIndexComment, comment);
                    break;
                case 7:
                    System.out.print("Enter username: ");
                    String user6 = scanner.nextLine();
                    socialNetwork.showSocialFeed(user6);
                    break;
                case 8:
                    System.out.print("Enter start user: ");
                    String startUser = scanner.nextLine();
                    System.out.print("Enter end user: ");
                    String endUser = scanner.nextLine();
                    socialNetwork.findShortestPath(startUser, endUser);
                    break;
                case 9:
                    System.out.print("Enter username: ");
                    String user7 = scanner.nextLine();
                    socialNetwork.suggestFriends(user7);
                    break;
                case 10:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}