package com.example.hestia_app.presentation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hestia_app.R;
import com.example.hestia_app.data.api.callbacks.RegistroPagamentoCallback;
import com.example.hestia_app.data.services.PagamentoService;
import com.example.hestia_app.domain.models.Pagamento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PaymentDialogFragment extends DialogFragment {

    private Button paymentDoneButton, stillPayingButton;
    private TextView copyCodeTextView, countdownTextView;
    private ImageView qrCodeImageView;

    // Service
    private PagamentoService pagamentoService;

    private final String PIX_KEY = "00020126360014br.gov.bcb.pix0114+5511970566577520400005303986540519.995802BR5925LAURA FARIAS DOMINGUES6009Sao Paulo62070503***630414C7";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_payment, container, false);

        pagamentoService = new PagamentoService(requireContext());

        paymentDoneButton = view.findViewById(R.id.paymentDoneButton);
        stillPayingButton = view.findViewById(R.id.stillPayingButton);
        copyCodeTextView = view.findViewById(R.id.copyCodeTextView);
        countdownTextView = view.findViewById(R.id.countdownTextView);
        qrCodeImageView = view.findViewById(R.id.qrCodeImageView);

        FirebaseAuth autenticar = FirebaseAuth.getInstance();
        FirebaseUser user = autenticar.getCurrentUser();

        String emailUser = user.getEmail();
        String nomeUser = user.getDisplayName();

        // Chamada para registrar um pagamento na API
        Pagamento pagamento = new Pagamento(emailUser, nomeUser);

        pagamentoService.registrarPagamento(pagamento, new RegistroPagamentoCallback() {
            @Override
            public void onRegistroSuccess(boolean sucesso, Pagamento pagamentoResponse) {
                if (sucesso) {
                    Log.d("Pagamento", "Pagamento registrado com sucesso na API: " + pagamentoResponse);
                } else {
                    Log.e("Pagamento", "Falha ao registrar pagamento na API.");
                }
            }

            @Override
            public void onRegistroFailure(boolean isPaid) {
                Log.e("Pagamento", "Falha ao efetuar pagamento");

            }
        });


        // Inicializar o contador regressivo de 5 segundos
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTextView.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                paymentDoneButton.setVisibility(View.VISIBLE);
                stillPayingButton.setVisibility(View.VISIBLE);
                countdownTextView.setVisibility(View.GONE);


                paymentDoneButton.setOnClickListener(v -> {
                    getActivity().finish();
                    dismiss();
                });
            }
        }.start();

        // Configurar ação para o botão de copiar código
        copyCodeTextView.setOnClickListener(v -> copyKeyPixClick());

        return view;
    }

    private void copyKeyPixClick() {
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Pix Key", PIX_KEY);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(getContext(), "Chave de transferência copiada!", Toast.LENGTH_SHORT).show();
    }
}
