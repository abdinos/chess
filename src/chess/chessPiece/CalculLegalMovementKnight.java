package chess.chessPiece;

import chess.Movement.AttackMovement;
import chess.Movement.Movement;
import chess.Movement.NormalMovement;
import chess.Movement.AttackCheckMovement;
import chess.board.BoardGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CalculLegalMovementKnight implements InterfaceCalculLegalMovementChessPiece {

    private final static int[] POSSIBLE_MOVEMENT_POSITION = {-17, -15, -10, -6, 10, 15, 17};

    public Collection<Movement> findLegalMovements(final BoardGame boardGame, final boolean verifyCheckAttack, final ChessPiece chessPiece){
        final List<Movement> legalMovements = new ArrayList<>();
        PieceColor enemyPieceColor;

        if(chessPiece.getPieceColor().isWhite()){
            enemyPieceColor = PieceColor.BLACK;
        }
        else{
            enemyPieceColor = PieceColor.WHITE;
        }

        for(final int vectorPosition : POSSIBLE_MOVEMENT_POSITION){
            final int futurePosition = chessPiece.getPiecePosition() + vectorPosition;
            if(BoardGame.isValidPosition(futurePosition)) {
                if(isFirstColumnExclusionPosition(futurePosition, vectorPosition) ||
                        isSecondColumnExclusionPosition(futurePosition, vectorPosition) ||
                        isSeventhColumnExclusionPosition(futurePosition, vectorPosition) ||
                        isEightColumnExclusionPosition(futurePosition, vectorPosition)){
                    continue;
                }
                if (!boardGame.isCaseOccupied(futurePosition)) {
                    NormalMovement normalMovement = new NormalMovement(boardGame, chessPiece, futurePosition);
                    if(verifyCheckAttack){
                        if(!boardGame.isKingCheckAfterMovement(normalMovement,enemyPieceColor)){
                            legalMovements.add(normalMovement);
                        }
                    }
                    else {
                        legalMovements.add(normalMovement);
                    }
                }
                else{
                    final ChessPiece chessPieceAtFuturePosition = boardGame.getChessPieceAtPosition(futurePosition);
                    if(chessPieceAtFuturePosition != null) {
                        if (chessPieceAtFuturePosition.getPieceColor() != chessPieceAtFuturePosition.getPieceColor()) {
                            if(chessPieceAtFuturePosition instanceof King){
                                AttackCheckMovement attackCheckMovement = new AttackCheckMovement(boardGame,chessPiece, futurePosition, chessPieceAtFuturePosition);
                                if(verifyCheckAttack) {
                                    if (!boardGame.isKingCheckAfterMovement(attackCheckMovement,enemyPieceColor)) {
                                        legalMovements.add(attackCheckMovement);
                                    }
                                }
                                else{
                                    legalMovements.add(attackCheckMovement);
                                }
                            }
                            else{
                                AttackMovement attackMovement = new AttackMovement(boardGame, chessPiece, futurePosition, chessPieceAtFuturePosition);
                                if(verifyCheckAttack) {
                                    if (!boardGame.isKingCheckAfterMovement(attackMovement,enemyPieceColor)) {
                                        legalMovements.add(attackMovement);
                                    }
                                }
                                else{
                                    legalMovements.add(attackMovement);
                                }
                            }
                        }
                    }
                }
            }
        }
        return legalMovements;
    }

    private static boolean isFirstColumnExclusionPosition(final int currentPosition, final int vectorPosition){
        return (BoardGame.FIRST_COLUMN[currentPosition] && (vectorPosition == -17 || vectorPosition == -10 ||
                vectorPosition == 6 || vectorPosition == 15));
    }

    private static boolean isSecondColumnExclusionPosition(final int currentPosition, final int vectorPosition){
        return (BoardGame.SECOND_COLUMN[currentPosition] && (vectorPosition == -10 || vectorPosition == 6));
    }

    private static boolean isSeventhColumnExclusionPosition(final int currentPosition, final int vectorPosition){
        return (BoardGame.SEVENTH_COLUMN[currentPosition] && (vectorPosition == -6 || vectorPosition == 10));
    }

    private static boolean isEightColumnExclusionPosition(final int currentPosition, final int vectorPosition){
        return (BoardGame.EIGHT_COLUMN[currentPosition] && (vectorPosition == -15 || vectorPosition == -6 ||
                vectorPosition == 10 || vectorPosition == 17));
    }
}
