package groom.tricountClone.domain.expense;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

  private final ExpenseRepository expenseRepository = ExpenseRepository.getExpenseRepository();

  public List<Expense> searchAllExpense(long settlementId) {
    return expenseRepository.getAllExpensesBySettlementId(settlementId);
  }

  public Expense saveExpense(Expense expense) {
    return expenseRepository.saveExpense(expense);
  }
}
