package chess.board;

import chess.chessPiece.ChessPiece;

public final class AttackMovement extends Movement{

    private final ChessPiece attackPiece;

    public AttackMovement(final BoardGame boardGame, final ChessPiece chessPiece, final int futurePosition, final ChessPiece attackPiece) {
        super(boardGame, chessPiece, futurePosition);
        this.attackPiece = attackPiece;
    }
}