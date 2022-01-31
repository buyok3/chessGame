package pieces;

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
    };


    Alliance(final String name){}

    abstract int getDirection();
    public abstract boolean isBlack();
    abstract boolean isWhite();
}
