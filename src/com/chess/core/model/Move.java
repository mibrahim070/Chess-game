package com.chess.core.model;

public class Move {
    private Spot sourceSpot;
    private Spot destinationSpot;
    private MoveResult moveResult;

    public Move(Spot sourceSpot, Spot destinationSpot) {
        this.sourceSpot = sourceSpot;
        this.destinationSpot = destinationSpot;
    }

    public Move(Spot sourceSpot, Spot destinationSpot, MoveResult moveResult) {
        this.sourceSpot = sourceSpot;
        this.destinationSpot = destinationSpot;
        this.moveResult = moveResult;
    }

    public Spot getSourceSpot() {
        return sourceSpot;
    }

    public void setSourceSpot(Spot sourceSpot) {
        this.sourceSpot = sourceSpot;
    }

    public Spot getDestinationSpot() {
        return destinationSpot;
    }

    public void setDestinationSpot(Spot destinationSpot) {
        this.destinationSpot = destinationSpot;
    }

    public MoveResult getMoveResult() {
        return moveResult;
    }

    public void setMoveResult(MoveResult moveResult) {
        this.moveResult = moveResult;
    }
}
