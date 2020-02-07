package ua.redrain47.hw11.service;

import lombok.SneakyThrows;
import org.junit.Test;
import ua.redrain47.hw11.model.Skill;
import ua.redrain47.hw11.repository.SkillRepository;

import static org.mockito.Mockito.*;

public class SkillServiceTest {
    private static SkillRepository skillRepo = mock(SkillRepository.class);
    private SkillService skillService = new SkillService(skillRepo);
    private Skill testSkill = new Skill(null, null);

    @SneakyThrows
    @Test
    public void shouldInvokeSave() {
        skillService.addData(testSkill);
        verify(skillRepo, times(1)).save(testSkill);
    }

    @SneakyThrows
    @Test
    public void shouldInvokeGetById() {
        skillService.getDataById(1L);
        verify(skillRepo, times(1)).getById(1L);
    }

    @SneakyThrows
    @Test
    public void shouldInvokeGetAll() {
        skillService.getAllData();
        verify(skillRepo, times(1)).getAll();
    }

    @SneakyThrows
    @Test
    public void shouldInvokeUpdate() {
        skillService.updateDataById(testSkill);
        verify(skillRepo, times(1)).update(testSkill);
    }

    @SneakyThrows
    @Test
    public void shouldInvokeDelete() {
        skillService.deleteDataById(1L);
        verify(skillRepo, times(1)).deleteById(1L);
    }
}