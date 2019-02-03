package globalconstants;

public class ErrorMessages {

    public static final String GAME_DID_NOT_START_YET_ERROR = "The Game has not been initialized yet. Please wait for other Players to join.";
    public static final String GAME_HAS_ALREADY_STARTED_ERROR = "The Game has already started. Please wait for the other Players to finish their current game";
    public static final String NOT_ENOUGH_POINTS_ERROR = "You need to put down stones at least " + Constants.MIN_FIRST_MOVE_POINTS + " worth of points to go on";
    public static final String BAG_IS_EMPTY_ERROR = "You cannot draw another Stone from the Bag since its empty.";
    public static final String HAND_IS_FULL_ERROR = "You cannot draw another Stone since your hand is full";
    public static final String NOT_YOUR_TURN_ERROR = "You cannot make this move since its not your turn";

}