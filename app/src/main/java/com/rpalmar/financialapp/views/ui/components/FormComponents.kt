package com.rpalmar.financialapp.views.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpalmar.financialapp.views.ui.theme.Black
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.app.DatePickerDialog
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.platform.LocalContext
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.views.ui.theme.Red
import java.util.Calendar
import java.util.TimeZone
import androidx.compose.material3.DatePickerDialog

@Composable
fun FormSectionTitle(
    title: String,
    onBackClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBackClick != null) {
            Text(
                text = "< Back",
                color = White,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onBackClick() }
            )
        }

        Text(
            text = title,
            color = White,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}


/** ---------------------------------------------
 * GENERAL TEXTFIELD PARA FORMULARIOS
 * -------------------------------------------- */
@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    prefix: String? = null,
    onFocusChanged: ((Boolean) -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, color = Color.White) },
            singleLine = true,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                unfocusedPlaceholderColor = Color.White.copy(alpha = 0.3f),
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                focusedPrefixColor = Color.White,
                unfocusedPrefixColor = Color.White.copy(alpha = 0.7f),
                disabledTextColor = Color.White,
                disabledBorderColor = Color.White.copy(alpha = 0.5f),
                disabledLabelColor = Color.White.copy(alpha = 0.7f),
                disabledPlaceholderColor = Color.White.copy(alpha = 0.3f),
                disabledPrefixColor = Color.White.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(14.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            prefix = prefix?.let { { Text(text = it, color = Color.White) } },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    onFocusChanged?.invoke(focusState.isFocused)
                }
        )
        if (!errorMessage.isNullOrEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
        }
    }
}

/** ---------------------------------------------
 * DOUBLE FIELD
 * -------------------------------------------- */
@Composable
fun FormDoubleField(
    modifier: Modifier = Modifier,
    value: Double?,
    onValueChange: ((Double) -> Unit)? = null,
    label: String,
    errorMessage: String? = null,
    enabled: Boolean = true,
    prefix: String? = null
) {
    var text by remember { mutableStateOf(value?.toString() ?: "") }
    var isFocused by remember { mutableStateOf(false) }
    
    // Sync internal state with value prop when field is NOT being actively edited
    LaunchedEffect(value) {
        if (!isFocused) {
            text = value?.toString() ?: ""
        }
    }
    
    FormTextField(
        value = text,
        onFocusChanged = { focused -> isFocused = focused },
        onValueChange = { newValue ->
            // Regex pattern to validate double input:
            // - Allow empty string
            // - Allow optional minus sign at start
            // - Allow digits before and after optional decimal point
            // - Reject multiple decimal points or invalid characters
            val validDoublePattern = "^-?\\d*\\.?\\d*$".toRegex()
            
            if (newValue.isEmpty() || newValue.matches(validDoublePattern)) {
                text = newValue
                // Only notify parent if there's a callback and the value is valid
                onValueChange?.let { callback ->
                    newValue.toDoubleOrNull()?.let(callback)
                }
            }
            // If input doesn't match pattern, ignore it (don't update text)
        },
        label = label,
        keyboardType = KeyboardType.Number,
        errorMessage = errorMessage,
        modifier = modifier,
        prefix = prefix,
        enabled = enabled
    )
}

/** ---------------------------------------------
 * DATE FIELD (Muestra dd/MM/yyyy)
 * -------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDateField(
    modifier: Modifier = Modifier,
    value: Date?,
    onValueChange: (Date) -> Unit,
    label: String,
    errorMessage: String? = null
) {
    // Estado para mostrar/ocultar el diálogo
    var showDialog by remember { mutableStateOf(false) }

    // Formateador
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val textStr = value?.let { formatter.format(it) } ?: ""

    // CAMPO DE TEXTO
    Box(modifier = modifier) {
        FormTextField(
            value = textStr,
            onValueChange = { },
            label = label,
            keyboardType = KeyboardType.Text,
            enabled = false, // Deshabilitado para delegar el click
            readOnly = true,
            errorMessage = errorMessage,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
        )
        // Capa transparente para asegurar el click en toda el área
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showDialog = true }
        )
    }

    // LÓGICA DEL DIÁLOGO
    if (showDialog) {
        val dateState = rememberDatePickerState(
            initialSelectedDateMillis = value?.time ?: System.currentTimeMillis()
        )

        val confirmEnabled = remember { derivedStateOf { dateState.selectedDateMillis != null } }

        // --- TU CÓDIGO ACTUALIZADO ---
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        dateState.selectedDateMillis?.let { millis ->
                            // AJUSTE DE ZONA HORARIA (CRUCIAL):
                            val offset = TimeZone.getDefault().getOffset(millis)
                            // Opción más segura usando Calendar:
                            val calendar = Calendar.getInstance()
                            calendar.timeInMillis = millis
                            calendar.add(Calendar.MILLISECOND, offset)
                            onValueChange(calendar.time)
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("Aceptar", color = DarkGrey)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = DarkGrey)
                }
            }
        ) {
            DatePicker(
                state = dateState,
//                colors = DatePickerDefaults.colors(
//                    // --- HEADER ---
//                    headerBackgroundColor = Red,
//                    headerHeadlineContentColor = White,
//
//                    // --- CUERPO ---
//                    containerColor = DarkGrey,
//
//                    // Títulos
//                    titleContentColor = White,
//                    weekdayContentColor = White,
//
//                    // Días
//                    dayContentColor = White,
//
//                    // Selección
//                    selectedDayContainerColor = Red,
//                    selectedDayContentColor = White,
//
//                    // Hoy
//                    todayDateBorderColor = Red,
//                    todayContentColor = Red,
//
//                    // Navegación
//                    navigationContentColor = White,
//
//                    // Año
//                    yearContentColor = White,
//                    currentYearContentColor = Red,
//                    selectedYearContentColor = White,
//                    selectedYearContainerColor = Red
//                )
            )
        }
    }
}

/** ---------------------------------------------
 * GENERIC DROPDOWN
 * -------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormDropdown(
    modifier: Modifier = Modifier,
    label: String,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    itemDetail: ((T) -> String)? = null,
    itemIcon: ((T) -> ImageVector)? = null,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelection by remember { mutableStateOf(selectedItem) }

    LaunchedEffect(selectedItem) { currentSelection = selectedItem }

    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (enabled) Color.White else Color.Gray,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                // SOLO PERMITE CLICK SI enabled = true
                .clickable(enabled = enabled) { expanded = !expanded },
            colors = CardDefaults.cardColors(
                if (enabled) Color(0xFF1E1E1E) else Color(0xFF3A3A3A)
            ),
            shape = MaterialTheme.shapes.medium,
        ) {

            Box(
                modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if (enabled)
                            Color.White.copy(alpha = 0.5f)
                        else
                            Color.Gray.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono del item seleccionado
                    if (currentSelection != null && itemIcon != null) {
                        Icon(
                            imageVector = itemIcon(currentSelection!!),
                            contentDescription = null,
                            tint = if (enabled) Color.White else Color.Gray.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 12.dp)
                        )
                    }

                    Column {
                        Text(
                            text = currentSelection?.let { itemLabel(it) } ?: "Seleccione...",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (currentSelection != null && itemDetail != null) FontWeight.Bold else FontWeight.Normal,
                            color = if (!enabled)
                                Color.Gray.copy(alpha = 0.6f)
                            else if (currentSelection == null)
                                Color.White.copy(alpha = 0.5f)
                            else
                                Color.White
                        )

                        if (currentSelection != null && itemDetail != null) {
                            Text(
                                text = itemDetail(currentSelection!!),
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (enabled)
                                    Color.White.copy(alpha = 0.7f)
                                else
                                    Color.Gray.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded,
                        modifier = if (!enabled) Modifier.alpha(0.3f) else Modifier
                    )
                }
            }

            DropdownMenu(
                expanded = expanded && enabled,
                onDismissRequest = { expanded = false },
                containerColor = Color(0xFF2C2C2C),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Icono del item
                                if (itemIcon != null) {
                                    Icon(
                                        imageVector = itemIcon(item),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 12.dp)
                                    )
                                }
                                Column {
                                    Text(itemLabel(item), color = Color.White)
                                    if (itemDetail != null) {
                                        Text(
                                            text = itemDetail(item),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        },
                        onClick = {
                            currentSelection = item
                            expanded = false
                            onItemSelected(item)
                        }
                    )
                }
            }
        }
    }
}

/** ---------------------------------------------
 * COLOR PICKER
 * -------------------------------------------- */
@Composable
fun FormColorPicker(
    modifier: Modifier = Modifier,
    label: String = "Color",
    colors: List<Color>,
    selectedColor: Color?,
    onColorSelected: (Color) -> Unit,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {

        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            colors.chunked(6).forEach { rowColors ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowColors.forEach { color ->
                        val isSelected = (selectedColor == color)

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = color,
                                    shape = RoundedCornerShape(50)
                                )
                                .clickable { onColorSelected(color) }
                                .then(
                                    if (isSelected)
                                        Modifier
                                            .padding(2.dp)
                                            .background(
                                                color = Color.White,
                                                shape = RoundedCornerShape(50)
                                            )
                                    else Modifier
                                )
                        )
                    }
                }
            }
        }

        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


/** ---------------------------------------------
 * ICON PICKER
 * -------------------------------------------- */
@Composable
fun FormIconPicker(
    modifier: Modifier = Modifier,
    label: String = "Icono",
    icons: List<ImageVector>,
    selectedIcon: ImageVector?,
    onIconSelected: (ImageVector) -> Unit,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {

        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            icons.chunked(6).forEach { rowIcons ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowIcons.forEach { icon ->

                        val isSelected = (selectedIcon == icon)

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = DarkGrey, // Fondo neutro para ver el ícono
                                    shape = RoundedCornerShape(50)
                                )
                                .clickable { onIconSelected(icon) }
                                .then(
                                    if (isSelected)
                                        Modifier
                                            .padding(2.dp)
                                            .background(
                                                color = Color.White,
                                                shape = RoundedCornerShape(50)
                                            )
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }

        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/** ---------------------------------------------
 * BOTONES
 * -------------------------------------------- */
@Composable
fun FormButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    primary: Boolean = true,
    enable: Boolean = true
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (primary) White else DarkGrey,
            contentColor = if (primary) Color.Black else White
        ),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = enable
    ) {
        Text(text = text, fontSize = 16.sp, textAlign = TextAlign.Center)
    }
}

/** ---------------------------------------------
 * PREVIEW
 * -------------------------------------------- */
@Preview(showBackground = true)
@Composable
fun FormComponentsPreview() {
    var text by remember { mutableStateOf("") }
    var number by remember { mutableStateOf(0.0) }
    var selected by remember { mutableStateOf<String?>(null) }
    val options = listOf("Option A", "Option B", "Option C")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FormSectionTitle("New Register")

        FormTextField(
            value = text,
            onValueChange = { text = it },
            label = "Text Field"
        )

        FormDoubleField(
            value = number,
            onValueChange = { number = it },
            label = "Number Field"
        )

        FormDoubleField(
            value = number,
            onValueChange = { number = it },
            label = "Amount Field",
            prefix = "$"
        )

        FormDateField(
            value = Date(),
            onValueChange = {},
            label = "Date Field"
        )

        FormDropdown(
            label = "Dropdown",
            items = options,
            selectedItem = selected,
            onItemSelected = { selected = it },
            itemLabel = { it }
        )

        val colorList = listOf(
            Color(0xFFE57373), Color(0xFFF06292), Color(0xFFBA68C8),
            Color(0xFF9575CD), Color(0xFF7986CB), Color(0xFF64B5F6),
            Color(0xFF4FC3F7), Color(0xFF4DD0E1), Color(0xFF4DB6AC),
            Color(0xFF81C784), Color(0xFFAED581), Color(0xFFFFD54F)
        )

        var selectedColor by remember { mutableStateOf<Color?>(null) }

        FormColorPicker(
            label = "Color del elemento",
            colors = colorList,
            selectedColor = selectedColor,
            onColorSelected = { selectedColor = it }
        )

        val iconList = IconMapper.icons.values.toList();

        var selectedIcon by remember { mutableStateOf<ImageVector?>(null) }

        FormIconPicker(
            label = "Icono del elemento",
            icons = iconList,
            selectedIcon = selectedIcon,
            onIconSelected = { selectedIcon = it }
        )

        FormButton(
            text = "Submit",
            onClick = {},
            primary = true
        )
        FormButton(
            text = "Cancel",
            onClick = {},
            primary = false
        )
    }
}
