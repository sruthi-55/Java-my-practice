import java.util.List;

// association connects independent objects without ownership
// aggregation is a weak has-a relationship while composition is a strong lifecycle-dependent has-a relationship
// access modifiers control visibility through public, protected, package-private and private access

public class O07_RelationshipsAndAccess {
    public static void main(String[] args) {
        Developer developer = new Developer("Sruthi");
        Team team = new Team(List.of(developer));
        House house = new House("Kitchen");
        System.out.println(team.members() + " " + house.room().name());	// [Developer[name=Sruthi]] Kitchen
    }

    // inheritance models an is-a relationship but can create tighter coupling than composition or interfaces
    // public is globally accessible, private is class-only, package-private is package-only and protected adds subclass access
    // a protected member outside its package is accessed through the subclass inheritance relationship
    // packages organize related types and prevent naming conflicts; java.lang is imported automatically
}

record Developer(String name) {
}

record Team(List<Developer> members) {
}

final class House {
    private final Room room;

    House(String roomName) {
        room = new Room(roomName);
    }

    Room room() {
        return room;
    }
}

record Room(String name) {
}
