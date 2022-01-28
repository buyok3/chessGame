package pieces;

public enum Alliance {
    WHITE("White"){
        @Override
        int getDirection(){
            return -1;
        }

        @Override
        boolean isBlack() {
            return false;
        }

        @Override
        boolean isWhite() {
            return true;
        }
    },

    BLACK("Black"){
        @Override
        int getDirection(){
            return 1;
        }

        @Override
        boolean isBlack() {
            return true;
        }

        @Override
        boolean isWhite() {
            return false;
        }
    };


    Alliance(final String name){}

    abstract int getDirection();
    abstract boolean isBlack();
    abstract boolean isWhite();
}
