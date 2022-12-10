package ss.uno;

public abstract class AbstractPlayer implements Player {
    Deck hand;

    @Override
    public abstract void determineMove();
}


