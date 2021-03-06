package uk.ac.bbsrc.tgac.miso.core.service;

import java.io.IOException;

import uk.ac.bbsrc.tgac.miso.core.data.Partition;
import uk.ac.bbsrc.tgac.miso.core.data.Run;
import uk.ac.bbsrc.tgac.miso.core.data.RunPartition;

public interface RunPartitionService {

  public RunPartition get(Run run, Partition partition) throws IOException;

  public void save(RunPartition runPartition) throws IOException;

  public void deleteForRun(Run run) throws IOException;

}
