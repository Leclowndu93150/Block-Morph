package tfar.blockmorph;

public enum BlockRotation {
    NORTH(0),
    NORTHEAST(45),
    EAST(90),
    SOUTHEAST(135),
    SOUTH(180),
    SOUTHWEST(225),
    WEST(270),
    NORTHWEST(315);

    private final int degrees;

    BlockRotation(int degrees) {
        this.degrees = degrees;
    }

    public int getDegrees() {
        return degrees;
    }

    public BlockRotation next() {
        return values()[(ordinal() + 1) % values().length];
    }
}
