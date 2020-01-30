package ua.redrain47.hw11.repository.jdbc;

import lombok.SneakyThrows;
import ua.redrain47.hw11.exceptions.ConnectionIssueException;
import ua.redrain47.hw11.exceptions.DeletingReferencedRecordException;
import ua.redrain47.hw11.exceptions.SuchEntityAlreadyExistsException;
import ua.redrain47.hw11.model.Account;
import ua.redrain47.hw11.queries.AccountQueries;
import ua.redrain47.hw11.repository.AccountRepository;
import ua.redrain47.hw11.util.ConnectionUtil;
import ua.redrain47.hw11.util.ObjectMapper;

import java.sql.*;
import java.util.List;

public class JdbcAccountRepositoryImpl implements AccountRepository {
    private Connection connection;

    public JdbcAccountRepositoryImpl() throws ConnectionIssueException {
        try {
            connection = ConnectionUtil.getConnection();
        } catch (SQLException e) {
            throw new ConnectionIssueException(e);
        }
    }

    @Override
    public boolean save(Account newAccount)
            throws SuchEntityAlreadyExistsException, ConnectionIssueException {
        if (newAccount != null) {
            try (PreparedStatement preparedStatement = connection
                    .prepareStatement(AccountQueries.INSERT_QUERY)) {

                preparedStatement.setString(1, newAccount.getEmail());
                preparedStatement.setString(2,
                        newAccount.getAccountStatus().toString());

                preparedStatement.execute();

                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new SuchEntityAlreadyExistsException(e);
            } catch (SQLException e) {
                throw new ConnectionIssueException(e);
            }
        }

        return false;
    }

    @Override
    public Account getById(Long searchId) throws ConnectionIssueException {
        if (searchId == null) {
            return null;
        }

        try (PreparedStatement preparedStatement = connection
                .prepareStatement(AccountQueries.SELECT_BY_ID_QUERY)) {

            preparedStatement.setInt(1, searchId.intValue());

            ResultSet resultSet = preparedStatement.executeQuery();
            Account foundAccount = null;
            List<Account> accountList = ObjectMapper.mapToAccountList(resultSet);

            if (accountList != null && accountList.size() != 0) {
                foundAccount = accountList.get(0);
            }

            return foundAccount;
        } catch (SQLException e) {
            throw new ConnectionIssueException(e);
        }
    }

    @Override
    public List<Account> getAll() throws ConnectionIssueException {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(AccountQueries.SELECT_ALL_QUERY)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Account> accountList = ObjectMapper.mapToAccountList(resultSet);

            return (accountList.size() != 0) ? accountList : null;
        } catch (SQLException e) {
            throw new ConnectionIssueException(e);
        }
    }

    @Override
    public boolean update(Account updatedAccount)
            throws SuchEntityAlreadyExistsException, ConnectionIssueException {
        if (updatedAccount != null) {
            try (PreparedStatement preparedStatement = connection
                    .prepareStatement(AccountQueries.UPDATE_BY_ID_QUERY)) {
                preparedStatement.setString(1, updatedAccount.getEmail());
                preparedStatement.setString(2,
                        updatedAccount.getAccountStatus().toString());
                preparedStatement.setInt(3, updatedAccount.getId().intValue());

                preparedStatement.execute();

                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new SuchEntityAlreadyExistsException(e);
            } catch (SQLException e) {
                throw new ConnectionIssueException(e);
            }
        }

        return false;
    }

    @Override
    public boolean deleteById(Long deletedId) throws
            DeletingReferencedRecordException, ConnectionIssueException {
        if (deletedId != null) {
            try (PreparedStatement preparedStatement = connection
                    .prepareStatement(AccountQueries.DELETE_BY_ID_QUERY)) {
                preparedStatement.setInt(1, deletedId.intValue());
                preparedStatement.execute();

                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new DeletingReferencedRecordException(e);
            } catch (SQLException e) {
                throw new ConnectionIssueException(e);
            }
        }

        return false;
    }
}