package chess.chessPiece;

import chess.Movement.*;
import chess.board.BoardGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CalculLegalMovementKing implements InterfaceCalculLegalMovementChessPiece {

    private final static int[] POSSIBLE_MOVEMENT_POSITION = {-9, -8, -7, -1, 1, 7, 8 ,9};

    public Collection<Movement> findLegalMovements(final BoardGame boardGame, final boolean verifyCheckAttack, final ChessPiece chessPiece) {
        final List<Movement> legalMovements = new ArrayList<>();
        PieceColor enemyPieceColor;

        if(chessPiece.getPieceColor().isWhite()){
            enemyPieceColor = PieceColor.BLACK;
        }
        else{
            enemyPieceColor = PieceColor.WHITE;
        }

        for(final int vectorPosition : POSSIBLE_MOVEMENT_POSITION){
            int futurePosition = chessPiece.getPiecePosition() + vectorPosition;

            if(BoardGame.isValidPosition(futurePosition)){
                if(isFirstColumnExclusionPosition(futurePosition, vectorPosition) ||
                        isHeightColumnExclusionPosition(futurePosition, vectorPosition)){
                    continue;
                }
                if (!boardGame.isCaseOccupied(futurePosition)) {
                    NormalMovement normalMovement = new NormalMovement(boardGame, chessPiece, futurePosition);
                    if(verifyCheckAttack) {
                        if (!boardGame.isKingCheckAfterMovement(normalMovement,enemyPieceColor)) {
                            legalMovements.add(normalMovement);
                        }
                    }
                    else{
                        legalMovements.add(normalMovement);
                    }

                    // Search if castling with a rook is possible
                    if(!chessPiece.isPieceMove() && (vectorPosition == 1 || vectorPosition == -1)) {
                        futurePosition = chessPiece.getPiecePosition();
                        while(BoardGame.isValidPosition(futurePosition)){
                            futurePosition += vectorPosition;

                            if(boardGame.isCaseOccupied(futurePosition)){
                                ChessPiece chessPieceAtFuturePosition = boardGame.getChessPieceAtPosition(futurePosition);
                                if(chessPieceAtFuturePosition instanceof Rook && !chessPieceAtFuturePosition.isPieceMove()){
                                    if(chessPiece.getPiecePosition() < chessPieceAtFuturePosition.getPiecePosition()){
                                        futurePosition = chessPiece.getPiecePosition() + 2;
                                    }
                                    else{
                                        futurePosition = chessPiece.getPiecePosition() - 2;
                                    }
                                    CastleMovement castleMovement = new CastleMovement(boardGame, chessPiece, futurePosition, chessPieceAtFuturePosition);
                                    if(verifyCheckAttack) {
                                        if (!boardGame.isKingCheckAfterMovement(castleMovement,enemyPieceColor)) {
                                            legalMovements.add(castleMovement);
                                        }
                                    }
                                    else{
                                        legalMovements.add(castleMovement);
                                    }
                                    break;
                                }
                                else{
                                    break;
                                }
                            }
                            if(isFirstColumnExclusionPosition(futurePosition, vectorPosition) ||
                                    isHeightColumnExclusionPosition(futurePosition, vectorPosition)){
                                break;
                            }
                        }
                    }

                }
                else{
                    final ChessPiece chessPieceAtFuturePosition = boardGame.getChessPieceAtPosition(futurePosition);
                    if(chessPieceAtFuturePosition != null) {
                        if (chessPiece.getPieceColor() != chessPieceAtFuturePosition.getPieceColor()) {
                            if(chessPieceAtFuturePosition instanceof King){
                                AttackCheckMovement attackCheckMovement = new AttackCheckMovement(boardGame, chessPiece, futurePosition, chessPieceAtFuturePosition);
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
        return (BoardGame.FIRST_COLUMN[currentPosition] && (vectorPosition == -9 || vectorPosition == -1 ||
                vectorPosition == 7));
    }

    private static boolean isHeightColumnExclusionPosition(final int currentPosition, final int vectorPosition){
        return (BoardGame.EIGHT_COLUMN[currentPosition] && ((vectorPosition == -7 || vectorPosition == 1) ||
                vectorPosition == 9));
    }
}
