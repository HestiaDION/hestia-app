package com.example.hestia_app.presentation.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private final View viewToAnimate; // View a ser animada

    // Construtor para passar a View que será animada
    // Construtor atualizado para receber a View
    public OnSwipeTouchListener(Context context, View viewToAnimate) {
        this.viewToAnimate = viewToAnimate;
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    // Classe interna para detectar gestos específicos
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100; // Distância mínima para considerar um swipe
        private static final int SWIPE_VELOCITY_THRESHOLD = 100; // Velocidade mínima

        @Override
        public boolean onDown(MotionEvent e) {
            return true; // Necessário para garantir que os outros métodos sejam chamados
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX(); // Diferença no eixo X
            float diffY = e2.getY() - e1.getY(); // Diferença no eixo Y

            // Verifica se o swipe é predominantemente horizontal
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight(); // Swipe para a direita
                    } else {
                        onSwipeLeft(); // Swipe para a esquerda
                    }
                    return true;
                }
            }
            return false;
        }
    }

    // Animação para quando o swipe for para a esquerda
    public void onSwipeLeft() {
        viewToAnimate.animate()
                .translationX(-viewToAnimate.getWidth()) // Desliza para fora da tela pela esquerda
                .setDuration(1000) // Define a duração da animação
                .withEndAction(() -> viewToAnimate.setVisibility(View.GONE)) // Após a animação, remove o card
                .start();
    }

    // Animação para quando o swipe for para a direita
    public void onSwipeRight() {
        viewToAnimate.animate()
                .translationX(viewToAnimate.getWidth()) // Desliza para fora da tela pela direita
                .setDuration(300)
                .withEndAction(() -> viewToAnimate.setVisibility(View.GONE)) // Após a animação, remove o card
                .start();
    }
}
