package by.karpovich.shop.service.client;

import by.karpovich.shop.api.dto.characteristic.CharacteristicDto;
import by.karpovich.shop.exception.NotFoundModelException;
import by.karpovich.shop.jpa.entity.CharacteristicEntity;
import by.karpovich.shop.jpa.repository.CharacteristicRepository;
import by.karpovich.shop.mapping.CharacteristicMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacteristicServiceImpl implements CharacteristicService {


    private final CharacteristicRepository characteristicRepository;
    private final CharacteristicMapper characteristicMapper;

    @Override
    @Transactional
    public CharacteristicDto saveCharacteristic(CharacteristicDto dto) {
        var entity = characteristicMapper.mapEntityFromDto(dto);
        var savedEntity = characteristicRepository.save(entity);

        log.info("method save - characteristic with id = {} saved", savedEntity.getId());
        return characteristicMapper.mapDtoFromEntity(savedEntity);
    }

    @Override
    public CharacteristicDto findCharacteristicById(Long id) {
        var entity = characteristicRepository.findById(id).orElseThrow(
                () -> new NotFoundModelException(String.format("characteristic with id = %s not found", id)));

        log.info("method findById - characteristic found with id = {} ", entity.getId());
        return characteristicMapper.mapDtoFromEntity(entity);
    }

    @Override
    public List<CharacteristicDto> findAllCharacteristics() {
        var entities = characteristicRepository.findAll();

        log.info("method findAll - characteristic found  = {} ", entities.size());
        return characteristicMapper.mapListDtoFromListEntity(entities);
    }

    @Override
    @Transactional
    public CharacteristicDto updateCharacteristicsById(Long id, CharacteristicDto dto) {
        var entity = characteristicMapper.mapEntityFromDto(dto);
        entity.setId(id);
        var updatedEntity = characteristicRepository.save(entity);

        log.info("method update - characteristic with id = {} updated", updatedEntity.getId());
        return characteristicMapper.mapDtoFromEntity(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteCharacteristicById(Long id) {
        if (characteristicRepository.findById(id).isEmpty()) {
            throw new NotFoundModelException(String.format("characteristic with id = %s not found", id));
        } else {
            characteristicRepository.deleteById(id);
        }
        log.info("method deleteById - characteristic with id = {} deleted", id);
    }

    @Override
    public CharacteristicEntity findCharacterByIdWhichWillReturnModel(Long id) {
        return characteristicRepository.findById(id).orElseThrow(
                () -> new NotFoundModelException("Characteristic with id = " + id + "not found"));
    }
}
