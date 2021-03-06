package uk.ac.bbsrc.tgac.miso.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bbsrc.tgac.miso.core.data.Experiment;
import uk.ac.bbsrc.tgac.miso.core.data.Submission;
import uk.ac.bbsrc.tgac.miso.core.security.AuthorizationManager;
import uk.ac.bbsrc.tgac.miso.core.service.ExperimentService;
import uk.ac.bbsrc.tgac.miso.core.service.SubmissionService;
import uk.ac.bbsrc.tgac.miso.core.store.DeletionStore;
import uk.ac.bbsrc.tgac.miso.core.util.WhineyFunction;
import uk.ac.bbsrc.tgac.miso.persistence.SubmissionStore;

@Transactional(rollbackFor = Exception.class)
@Service
public class DefaultSubmissionService implements SubmissionService {

  @Autowired
  private SubmissionStore submissionStore;
  @Autowired
  private ExperimentService experimentService;
  @Autowired
  private AuthorizationManager authorizationManager;
  @Autowired
  private DeletionStore deletionStore;

  @Override
  public Submission get(long id) throws IOException {
    return submissionStore.get(id);
  }

  @Override
  public List<Submission> list() throws IOException {
    return submissionStore.listAll();
  }

  @Override
  public long create(Submission submission) throws IOException {
    submission.setExperiments(submission.getExperiments().stream().map(Experiment::getId)
        .map(WhineyFunction.rethrow(experimentService::get))
        .collect(Collectors.toSet()));
    return submissionStore.save(submission);
  }

  @Override
  public long update(Submission submission) throws IOException {
    Submission managed = submissionStore.get(submission.getId());
    managed.setAccession(submission.getAccession());
    managed.setAlias(submission.getAlias());
    managed.setCompleted(submission.isCompleted());
    managed.setDescription(submission.getDescription());
    managed.setSubmissionDate(submission.getSubmissionDate());
    managed.setTitle(submission.getTitle());
    managed.setVerified(submission.isVerified());
    return submissionStore.save(managed);
  }

  @Override
  public DeletionStore getDeletionStore() {
    return deletionStore;
  }

  @Override
  public AuthorizationManager getAuthorizationManager() {
    return authorizationManager;
  }

}
