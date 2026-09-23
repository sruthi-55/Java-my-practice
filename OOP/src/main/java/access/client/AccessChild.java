package access.client;

import access.base.AccessBase;

// outside the parent's package a subclass uses protected members through its own inheritance relationship
public class AccessChild extends AccessBase {
    public int inheritedProtected() { return this.protectedValue; }

    public int anotherChild(AccessChild child) { return child.protectedValue; }

    // protectedValue through an arbitrary AccessBase reference is not accessible here
    // packageValue and privateValue are not directly accessible from this package
}
