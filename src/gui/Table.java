package gui;

import engine.board.Board;
import engine.board.BoardUtils;
import engine.board.Move;
import engine.board.MoveTransition;
import engine.pieces.Piece;
import engine.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JDialog.setDefaultLookAndFeelDecorated;
import static javax.swing.SwingUtilities.*;

public class Table {

    private final JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final BoardPanel boardPanel;
    private final MoveLog moveLog;
    private Board chessBoard;
    private Piece sourceTile;
    private Piece movedPiece;
    private final String pieceIconPath;
    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 500);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500, 500);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

    private static final Table INSTANCE = new Table();

    private Table() {
        this.gameFrame = new JFrame("Chess");
        final JMenuBar tableMenuBar = new JMenuBar();
        populateMenuBar(tableMenuBar);
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setLayout(new BorderLayout());
        this.chessBoard = Board.createStandardBoard();
        this.pieceIconPath = "pics/simple/";
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        setDefaultLookAndFeelDecorated(true);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        center(this.gameFrame);
        this.gameFrame.setVisible(true);
    }

    public static Table get() {
        return INSTANCE;
    }

    private JFrame getGameFrame() {
        return this.gameFrame;
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    public void show() {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

    private void populateMenuBar(final JMenuBar tableMenuBar) {
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createOptionsMenu());
    }

    private static void center(final JFrame frame) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = frame.getSize().width;
        final int h = frame.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }

    private JMenu createFileMenu() {
        final JMenu filesMenu = new JMenu("File");
        filesMenu.setMnemonic(KeyEvent.VK_F);

        final JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitMenuItem.addActionListener(e -> {
            Table.get().getGameFrame().dispose();
            System.exit(0);
        });
        filesMenu.add(exitMenuItem);

        return filesMenu;
    }

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic(KeyEvent.VK_O);

        final JMenuItem resetMenuItem = new JMenuItem("New Game", KeyEvent.VK_P);
        resetMenuItem.addActionListener(e -> undoAllMoves());
        optionsMenu.add(resetMenuItem);

        final JMenuItem legalMovesMenuItem = new JMenuItem("Current State", KeyEvent.VK_L);
        legalMovesMenuItem.addActionListener(e -> {
            System.out.println(chessBoard.getWhitePieces());
            System.out.println(chessBoard.getBlackPieces());
            System.out.println(playerInfo(chessBoard.currentPlayer()));
            System.out.println(playerInfo(chessBoard.currentPlayer().getOpponent()));
        });
        optionsMenu.add(legalMovesMenuItem);

        final JMenuItem undoMoveMenuItem = new JMenuItem("Undo last move", KeyEvent.VK_M);
        undoMoveMenuItem.addActionListener(e -> {
            if (Table.get().getMoveLog().size() > 0) {
                undoLastMove();
            }
        });
        optionsMenu.add(undoMoveMenuItem);

        return optionsMenu;
    }

    private static String playerInfo(final Player player) {
        return ("Player is: " + player.getAlliance() + "\nlegal moves (" + player.getLegalMoves().size() + ") = " + player.getLegalMoves() + "\ninCheck = " +
                player.isInCheck() + "\nisInCheckMate = " + player.isInCheckMate() +
                "\nisCastled = " + player.isCastled()) + "\n";
    }

    private void undoAllMoves() {
        for (int i = Table.get().getMoveLog().size() - 1; i >= 0; i--) {
            final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
            this.chessBoard = this.chessBoard.currentPlayer().unMakeMove(lastMove).getToBoard();
        }
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(chessBoard);
    }

    private void undoLastMove() {
        final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
        this.chessBoard = this.chessBoard.currentPlayer().unMakeMove(lastMove).getToBoard();
        Table.get().getMoveLog().removeMove(lastMove);
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(chessBoard);
    }

    private void checkForEndOfTheGame(final Board board) {
        JDialog winDialog = new JDialog();
        JLabel label = new JLabel();
        winDialog.setAlwaysOnTop(true);
        winDialog.setSize(300, 100);
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = winDialog.getSize().width;
        final int h = winDialog.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2;
        winDialog.setLocation(x, y);

        if (board.currentPlayer().isInCheckMate()) {
            winDialog.setTitle(board.currentPlayer().getOpponent().toString() + " won!");
            label.setText("Congratulation! " + board.currentPlayer().getOpponent().toString() + " won the game!");
            winDialog.add(label);
            winDialog.setVisible(true);
        }
        if (board.currentPlayer().isInStaleMate()) {
            winDialog.setTitle("Draw!");
            label.setText("It's a draw! Stalemate!");
            winDialog.add(label);
            winDialog.setVisible(true);
        }
    }

    private class BoardPanel extends JPanel {

        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.decode("#8B4726"));
            validate();
        }

        void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel boardTile : boardTiles) {
                boardTile.drawTile(board);
                add(boardTile);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog {

        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        void clear() {
            this.moves.clear();
        }

        Move removeMove(final int index) {
            return this.moves.remove(index);
        }

        void removeMove(final Move move) {
            this.moves.remove(move);
        }

    }

    private class TilePanel extends JPanel {

        private final int tileId;

        TilePanel(final BoardPanel boardPanel,
                  final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            highlightTileBorder(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent event) {
                    if (isRightMouseButton(event)) {
                        sourceTile = null;
                        movedPiece = null;
                    } else if (isLeftMouseButton(event)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getPiece(tileId);
                            movedPiece = sourceTile;
                            if (movedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getPiecePosition(),
                                    tileId);
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getToBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            movedPiece = null;
                        }
                    }
                    invokeLater(() -> {
                        gameHistoryPanel.redo(chessBoard, moveLog);
                        takenPiecesPanel.redo(moveLog);
                        boardPanel.drawBoard(chessBoard);
                        checkForEndOfTheGame(chessBoard);
                    });
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                }
            });
            validate();
        }

        void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightTileBorder(board);
            validate();
            repaint();
        }

        private void highlightTileBorder(final Board board) {
            if (movedPiece != null &&
                    movedPiece.getPieceAllegiance() == board.currentPlayer().getAlliance() &&
                    movedPiece.getPiecePosition() == this.tileId) {
                setBorder(BorderFactory.createLineBorder(Color.cyan));
            } else {
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getPiece(this.tileId) != null) {
                try {
                    final BufferedImage image = ImageIO.read(new File(pieceIconPath +
                            board.getPiece(this.tileId).getPieceAllegiance().toString().charAt(0) + "" +
                            board.getPiece(this.tileId).toString() +
                            ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            if (BoardUtils.INSTANCE.FIRST_ROW.get(this.tileId) ||
                    BoardUtils.INSTANCE.THIRD_ROW.get(this.tileId) ||
                    BoardUtils.INSTANCE.FIFTH_ROW.get(this.tileId) ||
                    BoardUtils.INSTANCE.SEVENTH_ROW.get(this.tileId)) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardUtils.INSTANCE.SECOND_ROW.get(this.tileId) ||
                    BoardUtils.INSTANCE.FOURTH_ROW.get(this.tileId) ||
                    BoardUtils.INSTANCE.SIXTH_ROW.get(this.tileId) ||
                    BoardUtils.INSTANCE.EIGHTH_ROW.get(this.tileId)) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }
    }
}
