/* ExpensesTransaction is part of ExpensesReporter and models a single transaction.
 *   Copyright (C) 2014 Nicola Cimmino
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see http://www.gnu.org/licenses/.
 *
*/
package com.nicolacimmino.expensesreporter.app;

/**
 * Created by nicola on 03/05/14.
 */
public class ExpensesTransaction {

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSource() {

        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isSyncDone() {
        return syncDone;
    }

    public void setSyncDone(boolean syncDone) {
        this.syncDone = syncDone;
    }

    private String source;
    private String destination;
    private double amount;
    private boolean syncDone;

}
