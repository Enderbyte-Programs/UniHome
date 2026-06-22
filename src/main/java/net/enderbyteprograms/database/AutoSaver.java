package net.enderbyteprograms.database;

/**
 * Program Title: Database Autosaver
 *
 * Program summary: Automatically save a table
 *
 * Program Elements List:
 * TODO IM NOT IN ICT 12 ANY MROE!!
 *
 * @author Jordan Rahim
 * @version 1
 * @date 2026-06-21
 */
class AutoSaver implements Runnable {//begin class

    //instance vars
    private Table associatedTable;
    public Thread saverThread;
    public int updatePeriod;//Update period in seconds.


    /**
     * Summary: Create an autosaver from a table and with a period
     * @param table
     * @param period
     * @return none
     */
    public AutoSaver(Table table,int period) {//begin constructor

        associatedTable = table;
        updatePeriod = period;
        saverThread = new Thread(this,"table-autosaver-"+table.tableName);

    }//end constructor




    /**
     * Summary: start the autosaver
     * @param none
     * @return none
     */
    public void start() {//begin method

        saverThread.start();

    }//end method




    /**
     * Summary: Stop autosaving
     * @param nothing
     * @return nothing
     */
    public void stop() {//begin method

        saverThread.interrupt();

    }//end method






    /**
     * Summary: Thread runner for auto saver - actually do the saving
     * @param none
     * @return none
     */
    @Override
    public void run() {//begin method

        int tick;

        tick = 0;

        while (!this.saverThread.isInterrupted()) {//begin while

            tick++;
            if ((tick / 10) == updatePeriod) {//begin if

                tick = 0;
                associatedTable.saveData();

            }//end if

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }

        }//end while

    }//end method

}//end class
