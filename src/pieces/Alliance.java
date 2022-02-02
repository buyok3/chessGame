package pieces;

import player.BlackPlayer;
import player.Player;
import player.WhitePlayer;

public enum Alliance {
    WHITE("White"){
        @Override
        int getDirection(){
            return -1;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },

    BLACK("Black"){
        @Override
        int getDirection(){
            return 1;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };


    Alliance(final String name){}

    abstract int getDirection();
    public abstract boolean isBlack();
    abstract boolean isWhite();

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}