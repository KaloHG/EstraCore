package net.estra.EstraCore.model;

public enum Rank {

    /**
     * Owner Rank
     */
    OWNER,

    /**
     * Officers, these people can add and remove people from the group.
     */
    OFFICER,

    /**
     * Members, these people can access and modify reins
     */
    MEMBER,

    /**
     * For the future I guess, not going to work on this now.
     */
    GUEST
}
