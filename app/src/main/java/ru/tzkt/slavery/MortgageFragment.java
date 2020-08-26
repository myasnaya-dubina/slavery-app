package ru.tzkt.slavery;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import ru.tzkt.slavery.domain.DateFormatter;
import ru.tzkt.slavery.domain.MortgageCalculator;
import ru.tzkt.slavery.utils.FormatUtils;
import ru.tzkt.slavery.utils.MathUtils;
import ru.tzkt.slavery.utils.UtilsKt;

public class MortgageFragment extends Fragment {

    // создаем переменные класса
    private MortgageCalculator mortgageCalculator = new MortgageCalculator();
    private DateFormatter dateFormatter = new DateFormatter();
    private State state = new State();
    // переменные синего блока
    private TextView apartmentPriceTextView = null;
    private SeekBar apartmentPriceSeekBar = null;
    private TextView bottomApartmentPriceBorder = null;
    private TextView topApartmentPriceBorder = null;
    // переменные зеленого блока
    private TextView paymentTextView = null;
    private SeekBar paymentSeekBar = null;
    private TextView bottomPaymentBorder = null;
    private TextView topPaymentBorder = null;
    private TextView resultPaymentTextView = null;
    // переменные красного блока
    private TextView mortgagePercentTextView = null;
    private SeekBar mortgagePercentSeekBar = null;
    private TextView mortgageBottomPercentBorder = null;
    private TextView mortgageTopPercentBorder = null;
    private TextView mortgageResultTextView = null;

    private StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
    private SpannableStringBuilder spannableStringBuilder;
    private long time;
    private int newBorder;
    private int currentValue;
    private float currentValuePercent;
    private String date;
    private String formattedAmount;

    public static Fragment newInstance() {
        return new MortgageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mortgage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initState();
        applyStateForViews(view);
        initApartmentViews(view);
        initSalaryViews(view);
        initMortgagePercentView(view);
    }

    private void initApartmentViews(View view) {
        bottomApartmentPriceBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String header = requireContext().getString(R.string.number_input_dialog_in_rubles);
                UtilsKt.showInputNumberDialog(requireContext(), header, 0, new Function1<Integer, Unit>() {
                    @Override
                    public Unit invoke(Integer integer) {
                        newBorder = integer;
                        if (state.aptTopBorder < newBorder) {
                            UtilsKt.showInvalidBordersDialog(requireActivity());
                            return Unit.INSTANCE;
                        }
                        state.aptBottomBorder = newBorder;
                        formattedAmount = FormatUtils.formatAmount(integer);
                        bottomApartmentPriceBorder.setText(formattedAmount);
                        currentValue = MathUtils.lerp(state.aptBottomBorder, state.aptTopBorder, apartmentPriceSeekBar.getProgress());
                        UtilsKt.setRubAmount(apartmentPriceTextView, currentValue);
                        state.aptPrice = currentValue;
                        return Unit.INSTANCE;
                    }
                });
            }
        });
        topApartmentPriceBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String header = requireContext().getString(R.string.number_input_dialog_in_rubles);
                UtilsKt.showInputNumberDialog(requireContext(), header, 0, new Function1<Integer, Unit>() {
                    @Override
                    public Unit invoke(Integer integer) {
                        newBorder = integer;
                        if (state.aptBottomBorder > newBorder) {
                            UtilsKt.showInvalidBordersDialog(requireActivity());
                            return Unit.INSTANCE;
                        }
                        state.aptTopBorder = newBorder;
                        formattedAmount = FormatUtils.formatAmount(integer);
                        topApartmentPriceBorder.setText(formattedAmount);
                        currentValue = MathUtils.lerp(state.aptBottomBorder, state.aptTopBorder, apartmentPriceSeekBar.getProgress());
                        UtilsKt.setRubAmount(apartmentPriceTextView, currentValue);
                        state.aptPrice = currentValue;
                        return Unit.INSTANCE;
                    }
                });
            }
        });
        UtilsKt.setDefaultStyle(apartmentPriceSeekBar);
        apartmentPriceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                currentValue = MathUtils.lerp(state.aptBottomBorder, state.aptTopBorder, progress);
                state.aptPrice = currentValue;
                mortgageCalculator.setApartmentPrice(currentValue);
                time = mortgageCalculator.getEndTimeBySavings(state.salary);
                date = dateFormatter.formatDate(time);
                spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder
                        .append("откладывая, купишь в ")
                        .append(date, boldSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                resultPaymentTextView.setText(spannableStringBuilder);
                time = mortgageCalculator.getEndTimeByMortgage(state.salary, state.mortgagePercent);
                date = dateFormatter.formatDate(time);
                if (time == 1) {
                    mortgageResultTextView.setText(date);
                } else {
                    spannableStringBuilder = new SpannableStringBuilder();
                    spannableStringBuilder
                            .append("ипотеку погасишь в ")
                            .append(date, boldSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mortgageResultTextView.setText(spannableStringBuilder);
                }
                UtilsKt.setRubAmount(apartmentPriceTextView, currentValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initSalaryViews(View view) {
        bottomPaymentBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String header = requireContext().getString(R.string.number_input_dialog_in_rubles);
                UtilsKt.showInputNumberDialog(requireContext(), header, 0, new Function1<Integer, Unit>() {
                    @Override
                    public Unit invoke(Integer integer) {
                        newBorder = integer;
                        if (state.salaryTopBorder < newBorder) {
                            UtilsKt.showInvalidBordersDialog(requireActivity());
                            return Unit.INSTANCE;
                        }
                        state.salaryBottomBorder = newBorder;
                        formattedAmount = FormatUtils.formatAmount(integer);
                        bottomPaymentBorder.setText(formattedAmount);
                        currentValue = MathUtils.lerp(state.salaryBottomBorder, state.salaryTopBorder, paymentSeekBar.getProgress());
                        UtilsKt.setRubAmount(paymentTextView, currentValue);
                        state.salary = currentValue;
                        return Unit.INSTANCE;
                    }
                });
            }
        });

        topPaymentBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String header = requireContext().getString(R.string.number_input_dialog_in_rubles);
                UtilsKt.showInputNumberDialog(requireContext(), header, 0, new Function1<Integer, Unit>() {
                    @Override
                    // ?метод конвертирует введеное число в физическую границу сик бара
                    public Unit invoke(Integer integer) {
                        newBorder = integer;
                        if (state.salaryBottomBorder > newBorder) {
                            UtilsKt.showInvalidBordersDialog(requireActivity());
                            return Unit.INSTANCE;
                        }
                        state.salaryTopBorder = newBorder;
                        formattedAmount = FormatUtils.formatAmount(integer);
                        topPaymentBorder.setText(formattedAmount);
                        currentValue = MathUtils.lerp(state.salaryBottomBorder, state.salaryTopBorder, paymentSeekBar.getProgress());
                        UtilsKt.setRubAmount(paymentTextView, currentValue);
                        state.salary = currentValue;
                        return Unit.INSTANCE;
                    }
                });
            }
        });

        UtilsKt.setDefaultStyle(paymentSeekBar);
        paymentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                currentValue = MathUtils.lerp(state.salaryBottomBorder, state.salaryTopBorder, progress);
                state.salary = currentValue;
                time = mortgageCalculator.getEndTimeBySavings(state.salary);
                date = dateFormatter.formatDate(time);
                spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder
                        .append("откладывая, купишь в ")
                        .append(date, boldSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                resultPaymentTextView.setText(spannableStringBuilder);
                time = mortgageCalculator.getEndTimeByMortgage(state.salary, state.mortgagePercent);
                date = dateFormatter.formatDate(time);
                if (time == 1) {
                    mortgageResultTextView.setText(date);
                } else {
                    spannableStringBuilder = new SpannableStringBuilder();
                    spannableStringBuilder
                            .append("ипотеку погасишь в ")
                            .append(date, boldSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mortgageResultTextView.setText(spannableStringBuilder);
                }
                UtilsKt.setRubAmount(paymentTextView, currentValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initMortgagePercentView(View view) {
        mortgageBottomPercentBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String header = getString(R.string.mortgage_percent_header);
                UtilsKt.showInputNumberDialog(requireContext(), header, 0, new Function1<Integer, Unit>() {
                    @Override
                    public Unit invoke(Integer integer) {
                        newBorder = integer;
                        if (state.mortgagePercentTopBorder < newBorder) {
                            UtilsKt.showInvalidBordersDialog(requireActivity());
                            return Unit.INSTANCE;
                        }
                        state.mortgagePercentBottomBorder = newBorder;
                        formattedAmount = FormatUtils.formatPercent(integer);
                        mortgageBottomPercentBorder.setText(formattedAmount);
                        currentValuePercent = MathUtils.lerp(state.mortgagePercentBottomBorder, state.mortgagePercentTopBorder, mortgagePercentSeekBar.getProgress());
                        UtilsKt.setPercentAmount(mortgagePercentTextView, currentValuePercent);
                        state.mortgagePercent = currentValuePercent;

                        return Unit.INSTANCE;
                    }
                });
            }
        });

        mortgageTopPercentBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String header = getString(R.string.mortgage_percent_header);
                UtilsKt.showInputNumberDialog(requireContext(), header, 0, new Function1<Integer, Unit>() {
                    @Override
                    public Unit invoke(Integer integer) {
                        newBorder = integer;
                        if (state.mortgagePercentBottomBorder > newBorder) {
                            UtilsKt.showInvalidBordersDialog(requireActivity());
                            return Unit.INSTANCE;
                        }
                        state.mortgagePercentTopBorder = newBorder;
                        formattedAmount = FormatUtils.formatPercent(newBorder);
                        mortgageTopPercentBorder.setText(formattedAmount);
                        currentValuePercent = MathUtils.lerp(state.mortgagePercentBottomBorder, state.mortgagePercentTopBorder, mortgagePercentSeekBar.getProgress());
                        UtilsKt.setPercentAmount(mortgagePercentTextView, currentValuePercent);
                        state.mortgagePercent = currentValuePercent;

                        return Unit.INSTANCE;
                    }
                });
            }
        });

        UtilsKt.setDefaultStyle(mortgagePercentSeekBar);
        mortgagePercentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }
                currentValuePercent = MathUtils.lerp(state.mortgagePercentBottomBorder, state.mortgagePercentTopBorder, progress);
                state.mortgagePercent = currentValuePercent;
                time = mortgageCalculator.getEndTimeByMortgage(state.salary, state.mortgagePercent);
                String date = dateFormatter.formatDate(time);
                if (time == 1) {
                    mortgageResultTextView.setText(date);
                } else {
                    spannableStringBuilder = new SpannableStringBuilder();
                    spannableStringBuilder.append("ипотеку погасишь в ")
                            .append(date, boldSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mortgageResultTextView.setText(spannableStringBuilder);
                }
                UtilsKt.setPercentAmount(mortgagePercentTextView, currentValuePercent);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initState() {
        state.aptBottomBorder = 0;
        state.aptTopBorder = 20_000_000;
        state.aptPrice = 10_000_000;

        state.salary = 100_000;
        state.salaryBottomBorder = 0;
        state.salaryTopBorder = 200_000;

        state.mortgagePercent = 10;
        state.mortgagePercentBottomBorder = 7;
        state.mortgagePercentTopBorder = 15;
    }

    private void applyStateForViews(View view) {
        // находим вью синего блока
        apartmentPriceTextView = view.findViewById(R.id.apartmentPriceTextView);
        apartmentPriceSeekBar = view.findViewById(R.id.apartmentPriceSeekBar);
        bottomApartmentPriceBorder = view.findViewById(R.id.bottomApartmentPriceBorder);
        topApartmentPriceBorder = view.findViewById(R.id.topApartmentPriceBorder);
        // засунем стейт в вью синего блока
        UtilsKt.setRubAmount(apartmentPriceTextView, state.aptPrice);
        String formatted = FormatUtils.formatAmount(state.aptBottomBorder);
        bottomApartmentPriceBorder.setText(formatted);
        formatted = FormatUtils.formatAmount(state.aptTopBorder);
        topApartmentPriceBorder.setText(formatted);
        apartmentPriceSeekBar.setProgress(50);
        mortgageCalculator.setApartmentPrice(state.aptPrice);
        // -------------------------------------------------
        // находим вью зеленого блока
        paymentTextView = view.findViewById(R.id.paymentTextView);
        paymentSeekBar = view.findViewById(R.id.paymentSeekBar);
        resultPaymentTextView = view.findViewById(R.id.resultPaymentTextView);
        topPaymentBorder = view.findViewById(R.id.topPaymentBorder);
        bottomPaymentBorder = view.findViewById(R.id.bottomPaymentBorder);
        // засунем стейт в вью зеленого блока
        UtilsKt.setRubAmount(paymentTextView, state.salary);
        formatted = FormatUtils.formatAmount(state.salaryBottomBorder);
        bottomPaymentBorder.setText(formatted);
        formatted = FormatUtils.formatAmount(state.salaryTopBorder);
        topPaymentBorder.setText(formatted);
        paymentSeekBar.setProgress(50);
        // -------------------------------------------------
        // находим вью красного блока
        mortgagePercentTextView = view.findViewById(R.id.mortgagePercentTextView);
        mortgagePercentSeekBar = view.findViewById(R.id.mortgagePercentSeekBar);
        mortgageBottomPercentBorder = view.findViewById(R.id.bottomMortgagePercentBorder);
        mortgageTopPercentBorder = view.findViewById(R.id.topMortgagePercentBorder);
        mortgageResultTextView = view.findViewById(R.id.mortgageResultTextView);
        // засунем стейт в вью красного блока
        UtilsKt.setPercentAmount(mortgagePercentTextView, state.mortgagePercent);
        formatted = FormatUtils.formatAmount(state.mortgagePercentBottomBorder);
        mortgageBottomPercentBorder.setText(formatted);
        formatted = FormatUtils.formatAmount(state.mortgagePercentTopBorder);
        mortgageTopPercentBorder.setText(formatted);
        mortgagePercentSeekBar.setProgress(80);
    }

    private static class State {

        public int aptPrice;
        public int aptBottomBorder;
        public int aptTopBorder;

        public int salary;
        public int salaryBottomBorder;
        public int salaryTopBorder;

        public float mortgagePercent;
        public float mortgagePercentBottomBorder;
        public float mortgagePercentTopBorder;
    }
}